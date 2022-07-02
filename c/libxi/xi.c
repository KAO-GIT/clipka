//libxi-dev
//
//gcc -o xi xi.c -lX11 -lXext -lXi
//gcc -m64 -fPIC -shared -o libxi.so xi.c -lX11 -lXext -lXi

#include <stdio.h>
#include <string.h>
#include <X11/XKBlib.h>
#include <X11/extensions/XInput2.h>

/* Return xi_opcode or -1 if X Input extension not available
*/
int get_xi_opcode(Display *dpy)
{
    int xi_opcode;
    int event, error;

    if (!XQueryExtension(dpy, "XInputExtension", &xi_opcode, &event, &error)) {
       //printf("X Input extension not available.\n");
          return -1;
    }

    return xi_opcode;
}

/* Return
   0 if XI2 is available,
   -1 - X Input extension not available,
   -2 Internal Error,
   (major<<8 | minor) versons if XI2 not support
*/
int check_xi2(Display *dpy)
{
    int major, minor;
    int rc;

    /* We support XI 2.2 */
    major = 2;
    minor = 2;

    rc = XIQueryVersion(dpy, &major, &minor);
    if (rc == BadRequest) {
    //printf("No XI2 support. Server supports version %d.%d only.\n", major, minor);
    return (major<<8) | minor;
    } else if (rc != Success) {
    //fprintf(stderr, "Internal Error! This is a bug in Xlib.\n");
    return -2;
    }
    //printf("XI2 supported. Server provides version %d.%d.\n", major, minor);
    return 0;
}

void select_events(Display *dpy, Window win)
{
    XIEventMask evmasks[1];
    unsigned char mask1[(XI_LASTEVENT + 7)/8];

    memset(mask1, 0, sizeof(mask1));

    /* select for key events from all master devices */

    // only release
    XISetMask(mask1, XI_RawKeyRelease);
    //XISetMask(mask1, XI_RawKeyPress);

    evmasks[0].deviceid = XIAllMasterDevices;
    evmasks[0].mask_len = sizeof(mask1);
    evmasks[0].mask = mask1;

    XISelectEvents(dpy, win, evmasks, 1);
    XFlush(dpy);
}

KeySym get_event(Display *dpy, Window win,int xi_opcode)
{
    //unsigned int mask;
    XEvent ev;

    KeySym s =  NoSymbol;

    XGenericEventCookie *cookie = &ev.xcookie;
    XNextEvent(dpy, &ev);

    if (cookie->type != GenericEvent ||
        cookie->extension != xi_opcode ||
        !XGetEventData(dpy, cookie))
        return s;

    switch (cookie->evtype) {
    case XI_RawKeyPress:
    case XI_RawKeyRelease:

       XIRawEvent *ev = cookie->data;
       s = XkbKeycodeToKeysym(dpy, ev->detail, 0, 0);
       break;
    }
    XFreeEventData(dpy, cookie);
    return s;
}

int main (int argc, char **argv)
{
    Display *dpy;

    dpy = XOpenDisplay(NULL);

    if (!dpy) {
    fprintf(stderr, "Failed to open display.\n");
    return -1;
    }

    int xi_opcode = get_xi_opcode(dpy);
    if (xi_opcode<0)
    {
       printf("X Input extension not available.\n");
       return -1;
    }

    int xi = check_xi2(dpy);
    if (xi!=0)
    {
       if(xi==-2) printf("Internal Error! This is a bug in Xlib.\n");
       if(xi>0) printf("No XI2 support. Server supports version %d.%d only.\n", xi>>8, xi|0xff);

       return -1;
    }

    Window wnd = DefaultRootWindow(dpy);

    /* select for XI2 events */
    select_events(dpy, wnd);

    while(1)
    {
       KeySym s = get_event(/* Display * */dpy, /* Window */wnd, xi_opcode);
       if (s == NoSymbol) continue;

          char *str = XKeysymToString(s);
          if (str == NULL) continue;
          if (strncmp(str, "Escape", 6) == 0) {
            fprintf(stderr, "Esc получен. Выход из программы.\n", str);
            return(0);
          } else
            fprintf(stderr, "keyEvent: \'%s\' %x. \n", str, s);
    }

    return 0;
}