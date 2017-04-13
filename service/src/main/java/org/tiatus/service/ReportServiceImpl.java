package org.tiatus.service;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.*;

import javax.inject.Inject;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;

/**
 * Created by johnreynolds on 07/04/2017.
 */
public class ReportServiceImpl implements ReportService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportServiceImpl.class);
    private static final String JBOSS_HOME_DIR = "jboss.home.dir";

    private EntryService entryService;
    private RaceService raceService;
    private TimesService timesService;
    private ConfigService configService;

    @Inject
    public ReportServiceImpl(ConfigService configService, EntryService entryService, RaceService raceService, TimesService timesService) {
        this.configService = configService;
        this.entryService = entryService;
        this.raceService = raceService;
        this.timesService = timesService;
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
        List<Entry> entries = entryService.getEntriesForRace(race);
        List<EntryPositionTime> times = timesService.getAllTimesForRace(race);
        // create array of entries by starting and end position
        List<EntriesForEventPositions> entriesByEventPositions = getEntriesSortedForEventPositions(entries);
        createByTimePdfReport(title, logoFile, race, now, entriesByEventPositions, times);
        createByEventPdfReport(title, logoFile, race, now, entriesByEventPositions, times);
    }

    private List<EntriesForEventPositions> getEntriesSortedForEventPositions(List<Entry> entries) {
        List<EntriesForEventPositions> list = new ArrayList<>();
        for (Entry entry: entries) {
            if (list.size() == 0) {
                EntriesForEventPositions ep = new EntriesForEventPositions();
                ep.setPositions(entry.getEvent().getPositions());
                ep.addEntry(entry);
                list.add(ep);
                continue;
            }

            EntriesForEventPositions matched = null;
            for (EntriesForEventPositions eventPosition: list) {
                if (doPositionsMatch(eventPosition.getPositions(), entry.getEvent().getPositions())) {
                    matched = eventPosition;
                    break;
                }
            }

            if (matched != null) {
                matched.addEntry(entry);
            } else {
                EntriesForEventPositions ep = new EntriesForEventPositions();
                ep.setPositions(entry.getEvent().getPositions());
                ep.addEntry(entry);
                list.add(ep);
            }
        }
        return list;
    }

    private boolean doPositionsMatch(List<EventPosition> positions, List<EventPosition> positionsForEvent) {
        if (positions.size() == positionsForEvent.size()) {
            for (int i = 0; i < positions.size(); i++) {
                EventPosition position = positions.get(i);
                EventPosition positionForEvent = positionsForEvent.get(i);
                if (position.getPosition().getId() != positionForEvent.getPosition().getId()) {
                    return false;
                }
                if (position.getPositionOrder() != positionForEvent.getPositionOrder()) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    private void createByTimePdfReport(String title, File logoFile, Race race, Date now, List<EntriesForEventPositions> entriesByEventPositions, List<EntryPositionTime> times) throws ServiceException, IOException, URISyntaxException {
        String fileName = "/tiatus/results/" + race.getName() + "_ByTime.pdf";
        File resultsFile = new File(System.getProperty(JBOSS_HOME_DIR) + fileName);
        resultsFile.getParentFile().mkdirs();
        // sort by time
        createPdfReport(resultsFile, title, logoFile, now, entriesByEventPositions);
    }

    private void createByEventPdfReport(String title, File logoFile, Race race, Date now, List<EntriesForEventPositions> entriesByEventPositions, List<EntryPositionTime> times) throws ServiceException, IOException, URISyntaxException {
        String fileName = "/tiatus/results/" + race.getName() + "_ByEvent.pdf";
        File resultsFile = new File(System.getProperty(JBOSS_HOME_DIR) + fileName);
        resultsFile.getParentFile().mkdirs();
        // sort by event
        createPdfReport(resultsFile, title, logoFile, now, entriesByEventPositions);
    }

    private PDImageXObject getLogoImage(File logoFile, PDDocument document) throws IOException {
        PDImageXObject pdImage = PDImageXObject.createFromFileByExtension(logoFile, document);
        return pdImage;
    }

    private void fillHeader(PDDocument document, PDPage page, PDImageXObject pdImage, String title, float scale, float yStartNewPage) throws IOException {
        PDFont font = PDType1Font.HELVETICA_BOLD;
        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true);
        contentStream.drawImage(pdImage, 20, yStartNewPage - (pdImage.getHeight() * scale), pdImage.getWidth()*scale, pdImage.getHeight()*scale);

        contentStream.beginText();
        contentStream.setFont(font, 12);
        contentStream.newLineAtOffset((pdImage.getWidth()*scale) + 60, yStartNewPage - (pdImage.getHeight() * scale)/2 + 20);
        contentStream.showText(title);
        contentStream.endText();

        contentStream.close();
    }

    private void fillFooter(PDDocument document, Date now, float margin) throws IOException {
        PDPageTree pages = document.getDocumentCatalog().getPages();
        int numberOfPages = pages.getCount();
        int count = 1;
        Iterator<PDPage> iterator = pages.iterator();
        PDFont footerFont = PDType1Font.TIMES_ROMAN;
        while (iterator.hasNext()) {
            PDPage pdPage = iterator.next();
            String text = "Page " + count + " of " + numberOfPages;
            PDPageContentStream footer = new PDPageContentStream(document, pdPage, PDPageContentStream.AppendMode.APPEND, true);
            footer.beginText();
            footer.setFont(footerFont, 10);
            footer.newLineAtOffset(pdPage.getMediaBox().getWidth()/2 - (text.length()/2), margin);
            footer.showText(text);
            footer.endText();
            footer.close();

            PDPageContentStream footerDate = new PDPageContentStream(document, pdPage, PDPageContentStream.AppendMode.APPEND, true);
            footerDate.beginText();
            footerDate.setFont(footerFont, 6);
            footerDate.newLineAtOffset(pdPage.getMediaBox().getWidth()/2 + 200, margin);
            footerDate.showText("(" + now + ")");
            footerDate.endText();
            footerDate.close();

            count++;
        }
    }

    private Cell<PDPage> createCellForRow(Row<PDPage> row, int width, String data, boolean fastestInSection) {
        Cell<PDPage> cell = row.createCell(width, data);
        cell.setFont(PDType1Font.HELVETICA);
        cell.setFontSize(10);
        if (fastestInSection) {
            cell.setFillColor(Color.YELLOW);
        }
        return cell;
    }

    private void createPdfReport(File resultsFile, String title, File logoFile, Date now, List<EntriesForEventPositions> entriesByEventPositions) throws ServiceException, IOException, URISyntaxException {

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
        document.addPage(page);

        PDImageXObject pdImage = getLogoImage(logoFile, document);

        float margin = 10;
        float scale = 0.15f;
        float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);

        fillHeader(document, page, pdImage, title, scale, yStartNewPage);

        float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
        boolean drawContent = true;
        float yStart = yStartNewPage - (pdImage.getHeight() * scale) - 10;
        float bottomMargin = 20;

        boolean firstTable = true;
        for (EntriesForEventPositions e: entriesByEventPositions) {
            if (firstTable) {
                firstTable = false;
            } else {
                page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
                document.addPage(page);
                yStart = yStartNewPage - 10;
            }
            BaseTable table = new BaseTable(yStart, yStartNewPage, bottomMargin, tableWidth, margin, document, page, true, drawContent);

            Row<PDPage> headerRow = table.createRow(15f);
            String positions = null;
            if (e.getPositions().size() > 0) {
                positions = e.getPositions().get(0).getPosition().getName() + " to " + e.getPositions().get(e.getPositions().size() - 1).getPosition().getName();
            }
            Cell<PDPage> cell = headerRow.createCell(100, positions);
            cell.setFont(PDType1Font.HELVETICA_BOLD);
            cell.setFillColor(Color.BLACK);
            cell.setTextColor(Color.WHITE);
            table.addHeaderRow(headerRow);
            List<Entry> entries = e.getEntries();
            fillResults(entries, table);
            yStart = table.draw();
        }

        fillFooter(document, now, margin);

        document.save(resultsFile);
        document.close();
    }

    private void fillResults(List<Entry> entries, BaseTable table) {
        // need to scale based on the number of positions all has to add to 100
        int numberOfPositions = entries.get(0).getEvent().getPositions().size() - 1;
        float totalColumnWidths = 80f;
        if (numberOfPositions > 0) {
            totalColumnWidths += numberOfPositions * 10;
        }
        float onePercentWidth = 100 / totalColumnWidths;
        int numberWidth = (int)(10 * onePercentWidth);
        int eventWidth = (int)(20 * onePercentWidth);
        int clubWidth = (int)(20 * onePercentWidth);
        int crewWidth = (int)(10 * onePercentWidth);
        int positionWidth = (int)(10 * onePercentWidth);
        int commentWidth = (int)(20 * onePercentWidth);
        int left;
        if (numberOfPositions > 0) {
            left = 100 - (numberWidth + eventWidth + clubWidth + crewWidth + commentWidth + (positionWidth * numberOfPositions));
        } else {
            left = 100 - (numberWidth + eventWidth + clubWidth + crewWidth + commentWidth);
        }
        commentWidth += left;
        for (Entry entry: entries) {
            Row<PDPage> row = table.createRow(10f);
            boolean fastestInSection = false;
            Cell<PDPage> number = createCellForRow(row, numberWidth, entry.getNumber().toString(), fastestInSection);
            Cell<PDPage> event = createCellForRow(row, eventWidth, entry.getEvent().getName(), fastestInSection);
            Cell<PDPage> club = createCellForRow(row, clubWidth, getClubsForEntry(entry), fastestInSection);
            Cell<PDPage> crew = createCellForRow(row, crewWidth, entry.getCrew(), fastestInSection);
            for (int i = 1; i < entry.getEvent().getPositions().size(); i++) {
                Cell<PDPage> position = createCellForRow(row, positionWidth, "", fastestInSection);
            }
            Cell<PDPage> comment = createCellForRow(row, commentWidth, "", fastestInSection);
        }
    }

    private String getClubsForEntry(Entry entry) {
        StringBuffer buffer = new StringBuffer();
        for (Club club: entry.getClubs()) {
            if (buffer.length() > 0) {
                buffer.append("/");
            }
            buffer.append(club.getClubName());
        }
        return buffer.toString();
    }
}
