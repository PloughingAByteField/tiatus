package org.tiatus.service;

import java.io.InputStream;

public interface ConfigService {
   void setEventFooter(String footer) throws ServiceException;
   void setEventTitle(String title) throws ServiceException;
   String setEventLogo(InputStream stream, String fileName) throws ServiceException;
   String getEventTitle();
   String getEventLogo();
}
