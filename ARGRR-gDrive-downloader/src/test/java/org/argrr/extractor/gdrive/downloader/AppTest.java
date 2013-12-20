/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.argrr.extractor.gdrive.downloader;

import org.argrr.extractor.gdrive.downloader.DriveApi;
import junit.framework.TestCase;

/**
 *
 * @author cdesclau
 */
public class AppTest extends TestCase {
    
    public AppTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getConnection method, of class App.
     */
    public void testGetConnection() {
        System.out.println("getConnection");
        DriveApi.getConnection();
    }

    /**
     * Test of downloadFolder method, of class App.
     */
    public void testDownloadFolder() throws Exception {
        System.out.println("downloadFolder");
        DriveApi.downloadFolder();
    }

    
}
