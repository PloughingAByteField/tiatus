package org.tiatus.service;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.Race;

import javax.inject.Inject;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

/**
 * Created by johnreynolds on 07/04/2017.
 */
public class ReportServiceImpl implements ReportService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportServiceImpl.class);
    private static final String JBOSS_HOME_DIR = "jboss.home.dir";

    private RaceService raceService;
    private ConfigService configService;

    @Inject
    public ReportServiceImpl(ConfigService configService, RaceService raceService) {
        this.configService = configService;
        this.raceService = raceService;
    }

    @Override
    public void createReportForRace(Race race) {
        LOG.debug("Got report creation request for race " + race.getName());
        try {
            createPdfReports(race);
        } catch (ServiceException | IOException | URISyntaxException e) {
            LOG.warn("Failed to create report ", e);
        }
    }

    private void createPdfReports(Race race) throws ServiceException, IOException, URISyntaxException {
        Date now = new Date();
        String logoFileName = configService.getEventLogo();
        File logoFile = new File(System.getProperty(JBOSS_HOME_DIR) + logoFileName);
        String title = configService.getEventTitle() + " " + race.getName();
        createByTimePdfReport(title, logoFile, race, now);
        createByEventPdfReport(title, logoFile, race, now);
    }

    private void createByTimePdfReport(String title, File logoFile, Race race, Date now) throws ServiceException, IOException, URISyntaxException {
//        List<EntryResult> results = getResultDetailsForRaceByTime(race);

        String fileName = "/tiatus/results/" + race.getName() + "_ByTime.pdf";
        File resultsFile = new File(System.getProperty(JBOSS_HOME_DIR) + fileName);
        resultsFile.getParentFile().mkdirs();
        String reportType = "By Time";
        createPdfReport(resultsFile, title, logoFile, reportType, now);
    }

    private void createByEventPdfReport(String title, File logoFile, Race race, Date now) throws ServiceException, IOException, URISyntaxException {
//        List<EntryResult> results = getResultDetailsForRaceByEvent(race);
        String fileName = "/tiatus/results/" + race.getName() + "_ByEvent.pdf";
        File resultsFile = new File(System.getProperty(JBOSS_HOME_DIR) + fileName);
        resultsFile.getParentFile().mkdirs();
        String reportType = "By Event";
        createPdfReport(resultsFile, title, logoFile, reportType, now);
    }

    private PDImageXObject getLogoImage(File logoFile, PDDocument document) throws IOException {
        PDImageXObject pdImage = PDImageXObject.createFromFileByExtension(logoFile, document);
        return pdImage;
    }

    private void createPdfReport(File resultsFile, String title, File logoFile, String reportType, Date now) throws ServiceException, IOException, URISyntaxException {

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDImageXObject pdImage = getLogoImage(logoFile, document);

        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true);
        PDFont font = PDType1Font.HELVETICA_BOLD;
        PDFont dateFont = PDType1Font.HELVETICA;

        float margin = 10;
        float scale = 0.15f;
        float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
        float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
        boolean drawContent = true;
        float yStart = yStartNewPage - (pdImage.getHeight() * scale) - 10;
        float bottomMargin = 20;
        BaseTable table  = new BaseTable(yStart, yStartNewPage, bottomMargin, tableWidth, margin, document, page, true, drawContent);

        Row<PDPage> headerRow = table.createRow(15f);
        Cell<PDPage> cell = headerRow.createCell(100, title);
        cell.setFont(PDType1Font.HELVETICA_BOLD);
        cell.setFillColor(Color.BLACK);
        cell.setTextColor(Color.WHITE);

        contentStream.drawImage(pdImage, 20, yStartNewPage - (pdImage.getHeight() * scale), pdImage.getWidth()*scale, pdImage.getHeight()*scale);

        contentStream.beginText();
        contentStream.setFont(font, 12);
        contentStream.newLineAtOffset((pdImage.getWidth()*scale) + 60, yStartNewPage - (pdImage.getHeight() * scale)/2 + 20);
        contentStream.showText(title);
        contentStream.endText();

        contentStream.close();

        document.save(resultsFile);
        document.close();
    }
}
