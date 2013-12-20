package org.argrr.extractor.inria.raweb.extractor;

import org.argrr.extractor.utils.Config;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String rootPath = App.class.getClassLoader().getResource("./").getPath()+"../../../";
        
        System.out.println(new InriaRaweb(rootPath+Config.getVar("RAWEB_EXTRACTED_FILE")).getTeam());
    }
}