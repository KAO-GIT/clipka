package kao.res;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

public class FillResources_main
{

	public static void main(String args[]) throws InvalidPropertiesFormatException, FileNotFoundException, IOException
	{

		List<Class<?>> l = List.of(ResNames.class, ResErrors.class, kao.db.cmd.DBCommandNames.class, kao.db.fld.DataFieldNames.class,
				kao.cp.SourceClpNames.class, kao.tsk.act.TskActionNames.class, kao.res.ResNamesWithId.class);

		//l = List.of(kao.tsk.act.TskActionNames.class);
		
		
		FillResources fl = new FillResources("");
		Class<? extends Annotation> a = AnnotationDefValue.class;

		for (Class<?> c : l)
		{
			workClass(fl, a, c);
		}
		
		fl.save();

		FillResources flru = new FillResources("_ru");

		Class<? extends Annotation> aru = AnnotationDefValueRu.class;

		for (Class<?> c : l)
		{
			workClass(flru, aru, c);
		}

		flru.save();

		System.out.print("Ok");

	}

	private static void workClass(FillResources fl, Class<? extends Annotation> a, Class<?> c)
	{
		work(fl, a, c, "","");
		for (Class<?> cv : c.getDeclaredClasses())
		{
			 if(cv.getSimpleName().equals("Descriptions")) work(fl, a, cv,"DESCRIPTION_",""); 
			 if(cv.getSimpleName().equals("ContentDescriptions")) work(fl, a, cv,"DESCRIPTION_","_CONTENT"); 
		}
	}

	private static void work(FillResources fl, Class<? extends Annotation> a, Class<?> c, String prefix, String suffix)
	{
		for (Field field : c.getDeclaredFields())
		{
			if (field.isAnnotationPresent(a)) { 
				String value; 
				if(a == kao.res.AnnotationDefValue.class) value = ((AnnotationDefValue) field.getAnnotation(a)).value();
				else value = ((AnnotationDefValueRu) field.getAnnotation(a)).value();
				fl.setProp(prefix+field.getName()+suffix, value);
			}
		}
	}

} 
