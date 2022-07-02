// gcc -m64 -fPIC -shared -o libxclipwatch.so xclipwatch.c -lX11 -lXfixes
// gcc -m64 xclipwatch xclipwatch.c -lX11 -lXfixes

#include <X11/extensions/Xfixes.h>

#include <assert.h>

#include <stdio.h>
#include <string.h>
//#include <wchar.h>

#include <limits.h>
#include <X11/Xlib.h>
#include <X11/Xatom.h>
#include <X11/Xutil.h>

/*
Bool PrintSelection(Display *display, Window window, const char *bufname, const char *fmtname)
{
char *result;
unsigned long ressize, restail;
int resbits;
Atom bufid = XInternAtom(display, bufname, False),
fmtid = XInternAtom(display, fmtname, False),
propid = XInternAtom(display, "XSEL_DATA", False),
incrid = XInternAtom(display, "INCR", False);
XEvent event;

XConvertSelection(display, bufid, fmtid, propid, window, CurrentTime);
do {
XNextEvent(display, &event);
} while (event.type != SelectionNotify || event.xselection.selection != bufid);

if (event.xselection.property)
{
XGetWindowProperty(display, window, propid, 0, LONG_MAX/4, False, AnyPropertyType,
&fmtid, &resbits, &ressize, &restail, (unsigned char**)&result);

if (fmtid == incrid)
printf("Buffer is too large and INCR reading is not implemented yet.\n");
else
printf("%.*s", (int)ressize, result);

XFree(result);
return True;
}
else // request failed, e.g. owner can't convert to the target format
return False;
}
*/

//char comm[255];   // командная строка - не используется

typedef struct
{
char wndname[255];  // имя окна
char wndclass[255]; // класс окна - обычно совпадает с именем, поэтому пока не используем.
char title[255];    // заголовок окна
int top;
int left;           // верхняя-левая координата окна
} RetValues;


static void* x11property(Display *dpy, Window win, Atom prop, Atom type, int *nitems) {
	Atom type_ret;
	int format_ret;
	unsigned long items_ret;
	unsigned long after_ret;
	unsigned char *prop_data = 0;

	if (XGetWindowProperty(dpy, win, prop, 0, 0x7fffffff, False, type,
			&type_ret, &format_ret, &items_ret, &after_ret, &prop_data) != Success)
		return 0;

	if (nitems)
		*nitems = items_ret;

	return prop_data;
}


void  get_window_title(Display *display, const Window window, RetValues* r)
{
    XTextProperty text_property;
    Status status = XGetWMName(display, window, &text_property);
    if (!status || !text_property.value || !text_property.nitems) {
        strcpy(r->title,"");
        return ;
    }

    char **name_list;
    int count;
    status = Xutf8TextPropertyToTextList(display, &text_property, &name_list, &count);
    if (status < Success || !count) {
        XFree(text_property.value);
        strcpy(r->title,"");
        return ;
    }

    if (!name_list[0]) {
        XFreeStringList(name_list);
        XFree(text_property.value);
        strcpy(r->title,"");
        return ;
    }

    strncpy(r->title,name_list[0],255);
    XFreeStringList(name_list);
    XFree(text_property.value);
    return ;
}

void get_window_class_name(Display *display, const Window window, RetValues* r)
{
    XClassHint class_hint;
    Status result = XGetClassHint(display, window, &class_hint);
    if (result != 0) {
            
            if(class_hint.res_name != NULL) {strncpy(r->wndname,class_hint.res_name,255); XFree(class_hint.res_name); }
            if(class_hint.res_class != NULL){strncpy(r->wndclass,class_hint.res_class,255); XFree(class_hint.res_class);}
            
    } else
        fprintf(stderr, "couldn't get property WM_CLASS %d\n", result);
        
}

void get_window_coordinates(Display *display, const Window window, RetValues* r)
{
 int x=-1, y=-1;
 //uint width, height, border, depth;
 Window root, child;

 //XGetGeometry(display, window, &root, &x, &y, &width, &height, &border, &depth);
 //XTranslateCoordinates(display, window, root, x, y, &x, &y, &child);

 // просто получим DefaultRootWindow
 root = DefaultRootWindow(display);
 XTranslateCoordinates(display, window, root, 0, 0, &x, &y, &child);
 r->left = x;
 r->top  = y;
}


/*
void get_window_command(Display *display, const Window window, RetValues* r)
{
    char **cliargv = NULL;
    int cliargc;
    char *dest; 
    
    if (!XGetCommand (display, window, &cliargv, &cliargc)) {
    printf("- %d \n",cliargc);
    //strcpy(dest,""); 
 	for (int i = 0; i < cliargc; i++) {
 	//printf("- %d \n",i);
 	//printf("- %s \n",cliargv[i]);
 	//dest = strcat(dest,cliargv[i]); 
 	//dest = strcat(dest," "); 
    }
    XFreeStringList (cliargv);   
    //XFree(dest); 
    }
        
}
*/

void get_active_window_params(Display *display, RetValues* r)
{
      Window *winlist, activewin;
      Atom atom_active_window = XInternAtom(display, "_NET_ACTIVE_WINDOW", False);
      winlist = x11property(display, DefaultRootWindow(display), atom_active_window, XA_WINDOW, NULL);
      if (winlist) {
		activewin = *winlist;
		XFree(winlist);
	  } else {
		activewin = None;
	  }

      if (activewin != None)
      {
//      XTextProperty text;
//      XGetWMName(display, activewin, &text);
//      if(text.value!=NULL) strncpy(r->title,text.value,255);
//      XFree(text.value);

      get_window_title(display, activewin, r);
      get_window_class_name(display, activewin, r);
      get_window_coordinates(display, activewin, r);
      //get_window_command(display, activewin, r);
      };
}

int WatchSelection(int bufnum, RetValues* r)
{
  const char *bufname = (bufnum==1 ? "CLIPBOARD" : "PRIMARY");   
 
  Display *display = XOpenDisplay(NULL);

  int event_base, error_base;
  XEvent event;
  Atom bufid = XInternAtom(display, bufname, False);

  assert( XFixesQueryExtension(display, &event_base, &error_base) );
  XFixesSelectSelectionInput(display, DefaultRootWindow(display), bufid, XFixesSetSelectionOwnerNotifyMask);

  while (True)
  {
    XNextEvent(display, &event);

    if (event.type == event_base + XFixesSelectionNotify &&
        ((XFixesSelectionNotifyEvent*)&event)->selection == bufid)
    {

    get_active_window_params(display, r);

/*
      Window ownerwin = XGetSelectionOwner(display, bufid);
      if(ownerwin!=None)
      {
      XTextProperty text2;
      XGetWMName(display, ownerwin, &text2);
	  if(text2.value!=NULL) strncpy(r->name2,text2.value,255);
	  //printf("+++ %s  \n",text2.value);
      XFree(text2.value);
      }
*/

      break; 
    }
  }
  
  XCloseDisplay(display);

  return 0;

}



int main()
{
//Display *display = XOpenDisplay(NULL);
//unsigned long color = BlackPixel(display, DefaultScreen(display));
//Window window = XCreateSimpleWindow(display, DefaultRootWindow(display), 0,0, 1,1, 0, color, color);
//Window window = DefaultRootWindow(display);

//Bool result = PrintSelection(display, window, "CLIPBOARD", "UTF8_STRING") ||
//PrintSelection(display, window, "CLIPBOARD", "STRING");

//WatchSelection(display, window, "CLIPBOARD"); //PRIMARY

//char *name;
//char *name2;
//, &name, &name2

RetValues r;
WatchSelection(1, &r); //1 - CLIPBOARD, 2 - PRIMARY

printf("--- %s \n",r.title);
printf("--- %s \n",r.wndname);
printf("--- %s \n",r.wndclass);
printf("--- %d \n",r.left);
printf("--- %d \n",r.top);
//printf("--- %s \n",r.comm);

//XDestroyWindow(display, window);
//XCloseDisplay(display);
return 0;
}
