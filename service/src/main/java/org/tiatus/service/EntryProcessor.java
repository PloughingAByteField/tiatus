package org.tiatus.service;

import java.io.InputStream;

/**
 * Created by johnreynolds on 21/12/2015.
 */
public interface EntryProcessor {
    FileProcessingResult processEntriesFile(InputStream contents, String fileName) throws ServiceException;
}
