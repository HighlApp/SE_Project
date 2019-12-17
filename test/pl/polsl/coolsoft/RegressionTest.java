/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.coolsoft;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javafx.scene.control.TextField;
import org.junit.*;
import static org.junit.Assert.*;
import pl.polsl.largetableviewer.main.MainController;

/**
 *
 * @author Rex
 */
public class RegressionTest 
{
    static final String INPUT ="C:\\Users\\Rex\\Desktop\\Projekty\\SE\\Nb project\\input.txt";
    static final String OUTPUT ="C:\\Users\\Rex\\Desktop\\Projekty\\SE\\Nb project\\output.txt";
    static final String EXPECTED ="C:\\Users\\Rex\\Desktop\\Projekty\\SE\\Nb project\\expected.txt";
    static final String INPUT_FIELD_NAME="fileNameField";
    static final String OUTPUT_FIELD_NAME="outputFilePath";
    static final String INITIALIZE="initialize";
    
    private TextField inputTF;
    
    @Test
    public void regressionTest()
    {
        try
        {
            Object mainClass=MainController.class.newInstance();
            
            Method init = mainClass.getClass().getDeclaredMethod(INITIALIZE);
            init.setAccessible(true);
            
            Field inputFile = mainClass.getClass().getDeclaredField(INPUT_FIELD_NAME);
            Field outputFile = mainClass.getClass().getDeclaredField(OUTPUT_FIELD_NAME);
            inputFile.setAccessible(true);
            outputFile.setAccessible(true);
            
            fail(inputFile.toString());
            inputTF = (TextField) inputFile.get(mainClass);
            inputTF.setText(INPUT);
            //assertEquals("Może", inputTF,(TextField) inputFile.get(mainClass));
            //inputTF.setText(INPUT);
            
            //inputFile.set(mainClass,inputTF);
            //outputFile.set(mainClass, OUTPUT);
            
        }
        catch (InstantiationException e) 
        {  fail("Invocation error! " + e); }
        catch(IllegalAccessException e)
        {  fail("Invocation error! "+e); }
        catch(NoSuchFieldException e)
        {  fail("Invocation error! "+ e); }
        catch(NoSuchMethodException e)
        {  fail("Invocation error! "+e); }
    }
}
