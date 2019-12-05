/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.coolsoft;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;
import pl.polsl.coolsoft.controller.MainController;
import pl.polsl.coolsoft.controller.Range;


/**
 *
 * @author Rex
 */
public class TestClass 
{
    @Test
    public void stringToRangeProperValues()
    {
        String s1="1-5;2-3;";
        List<Range> r1=new ArrayList<>();
        r1.add(new Range(1,5));
        r1.add(new Range(2,3));
        
        try
        {
            Object mainClass=MainController.class.newInstance();
            
            Method cstr = mainClass.getClass().getDeclaredMethod("convertStringToRanges", String.class);
            cstr.setAccessible(true);
            Object o =  cstr.invoke(mainClass, s1);
            List<Range> outcome=(List<Range>)(Object)o;
            
            //manual check since Range has no Equals method
            if(r1.size()!=outcome.size())
                fail("The lists should be of equal size");
            for(int i=0; i<r1.size();i++)
            {
                if((r1.get(i).getFrom()!=outcome.get(i).getFrom())
                        ||(r1.get(i).getTo()!=outcome.get(i).getTo()))
                    fail("The lists doesn't match");
            }
        }
        catch(Exception e)
        {   
            fail(e.getClass().getName());
        }
    }
    
    @Test
    public void stringToRangeImproperValues()
    {
        String s1="1-j;kwejk;";
        
        try
        {
            Object mainClass=MainController.class.newInstance();
            
            Method cstr = mainClass.getClass().getDeclaredMethod("convertStringToRanges", String.class);
            cstr.setAccessible(true);
            cstr.invoke(mainClass, s1);
            
            fail("Method should throw an Exception");
        }
        catch(InvocationTargetException e)
        {
            if(new NumberFormatException().getClass().getName().equals(e.getCause().getClass().getName()))
            {
                //all is right        
            }
            else
                fail("The method has thrown undeclared Exception");
                    
        }
        catch(Exception e)
        {
            fail(e.getClass().getName());
        }
    }
}
