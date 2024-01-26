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
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiatus.entity.*;
import org.tiatus.entity.Event;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;

/**
 * Created by johnreynolds on 07/04/2017.
 */
@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportServiceImpl.class);
    private static final String JBOSS_HOME_DIR = "jboss.home.dir";

    @Autowired
    protected ConfigService configService;

    @Autowired
    protected DisqualificationService disqualificationService;

    @Autowired
    protected EventService eventService;

    @Autowired
    protected EntryService entryService;

    @Autowired
    protected PenaltyService penaltyService;

    @Autowired
    protected TimesService timesService;

    private List<Disqualification> disqualifications;
    private List<Penalty> penalties;
    private List<RaceEvent> raceEvents;
    private List<Entry> entries;
    private List<EntryPositionTime> times;

    private Locale currentLocale;
    private ResourceBundle messages;

    public ReportServiceImpl() {
        // TODO pull language and country from config service which is to be configured as part of setup
        String language = "en";
        String country = "GB";
        this.currentLocale = new Locale(language, country);
        this.messages = ResourceBundle.getBundle("org.tiatus.service.ReportService", currentLocale);
    }

    @Override
    public void createReportForRace(Race race) {
        LOG.debug("Got report creation request for race " + race.getName());
        try {
            disqualifications = disqualificationService.getDisqualifications();
            penalties = penaltyService.getPenalties();
            raceEvents = eventService.getRaceEventsForRace(race);
            entries = entryService.getEntriesForRace(race);
            times = timesService.getAllTimesForRace(race);
            Date now = new Date();
            createPdfReports(race, now);

        } catch (ServiceException | IOException | URISyntaxException e) {
            LOG.warn("Failed to create report ", e);
        }
    }

    private void createPdfReports(Race race, Date now) throws ServiceException, IOException, URISyntaxException {

        String logoFileName = configService.getEventLogo();
        File logoFile = new File(System.getProperty(JBOSS_HOME_DIR) + logoFileName);
        String title = configService.getEventTitle() + " " + race.getName();

        // create array of entries by starting and end position
        List<EntriesForEventPositions> entriesByEventPositions = getEntriesSortedForEventPositions(entries);

        createByTimePdfReport(title, logoFile, race, now, entriesByEventPositions);
        createByEventPdfReport(title, logoFile, race, now, entriesByEventPositions);
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

    private void createByTimePdfReport(String title, File logoFile, Race race, Date now, List<EntriesForEventPositions> entriesByEventPositions) throws ServiceException, IOException, URISyntaxException {
        String fileName = "/tiatus/results/" + race.getName() + "_ByTime.pdf";
        File resultsFile = new File(System.getProperty(JBOSS_HOME_DIR) + fileName);
        resultsFile.getParentFile().mkdirs();
        // sort by time
        for (EntriesForEventPositions e: entriesByEventPositions) {
            e.getEntries().sort(new Comparator<Entry>() {
                @Override
                public int compare(Entry e1, Entry e2) {
                    return compareEntries(e1, e2);
                }
            });
        }
        createPdfReport(resultsFile, title, logoFile, "by_time", now, entriesByEventPositions);
    }

    private void createByEventPdfReport(String title, File logoFile, Race race, Date now, List<EntriesForEventPositions> entriesByEventPositions) throws ServiceException, IOException, URISyntaxException {
        String fileName = "/tiatus/results/" + race.getName() + "_ByEvent.pdf";
        File resultsFile = new File(System.getProperty(JBOSS_HOME_DIR) + fileName);
        resultsFile.getParentFile().mkdirs();
        // sort by event
        for (EntriesForEventPositions e: entriesByEventPositions) {
            e.getEntries().sort(new Comparator<Entry>() {
                @Override
                public int compare(Entry e1, Entry e2) {
                    if (e1.getEvent().getId() == e2.getEvent().getId()) {
                        return compareEntries(e1, e2);
                    } else {
                        if (getRaceOrderForEvent(e1.getEvent()) < getRaceOrderForEvent(e2.getEvent())) {
                            return -1;
                        } else  if (getRaceOrderForEvent(e1.getEvent()) > getRaceOrderForEvent(e2.getEvent())) {
                            return 1;
                        }
                    }
                    return 0;
                }
            });
        }
        createPdfReport(resultsFile, title, logoFile, "by_event", now, entriesByEventPositions);
    }

    private int compareEntries(Entry entry1, Entry entry2) {
        List<EntryPositionTime> entry1Times = getPositionTimesForEntryByPosition(entry1, times);
        List<EntryPositionTime> entry2Times = getPositionTimesForEntryByPosition(entry2, times);
        if (entry1Times.size() == 0 && entry2Times.size() > 0) {
            return 1;
        }
        if (entry1Times.size() > 0 && entry2Times.size() == 0) {
            return -1;
        }

        boolean isEntry1Disqualified = isEntryDisqualified(entry1);
        boolean isEntry2Disqualified = isEntryDisqualified(entry2);
        if (isEntry1Disqualified != isEntry2Disqualified) {
            if (isEntry1Disqualified) {
                return 1;
            } else {
                return -1;
            }
        }

        List<EventPosition> positions = entry1.getEvent().getPositions();
        int numberOfPositions = positions.size();
        if (entry1Times.size() < numberOfPositions && entry2Times.size() == numberOfPositions) {
            return 1;
        }
        if (entry1Times.size() == numberOfPositions && entry2Times.size() < numberOfPositions) {
            return -1;
        }

        Duration entry1Time = getDurationForEntry(entry1, entry1Times);
        Duration entry2Time = getDurationForEntry(entry2, entry2Times);
        if (entry1Time != null && entry2Time != null) {
            return entry1Time.compareTo(entry2Time);
        }

        return 0;
    }

    private Duration getDurationForEntry(Entry entry, List<EntryPositionTime> timesForEntry) {
        List<EventPosition> positions = entry.getEvent().getPositions();
        if (positions.size() == 0) {
            return null;
        }
        Position startingPosition = positions.get(0).getPosition();
        Position finishingPosition = positions.get(positions.size() - 1).getPosition();
        Timestamp startTimestamp = getTimeForPosition(startingPosition, timesForEntry);
        Timestamp finishTimestamp = getTimeForPosition(finishingPosition, timesForEntry);
        if (startTimestamp != null && finishTimestamp != null) {
            Instant startTimestampInstant = startTimestamp.toInstant();
            Instant finishTimestampInstant = finishTimestamp.toInstant();
            finishTimestampInstant = addPenaltiesToTimeForEntry(finishTimestampInstant, entry);
            return Duration.between(startTimestampInstant, finishTimestampInstant);
        }

        return null;
    }

    private Integer getRaceOrderForEvent(Event event) {
        for (RaceEvent raceEvent: raceEvents) {
            if (event.getId() == raceEvent.getEvent().getId()) {
                return raceEvent.getRaceEventOrder();
            }
        }
        return 0;
    }

    private PDImageXObject getLogoImage(File logoFile, PDDocument document) throws IOException {
        PDImageXObject pdImage = PDImageXObject.createFromFileByExtension(logoFile, document);
        return pdImage;
    }

    private void fillHeader(PDDocument document, PDPage page, PDImageXObject pdImage, String title, float scale, float yStartNewPage, String reportType, Date now) throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true);
        contentStream.drawImage(pdImage, 20, yStartNewPage - (pdImage.getHeight() * scale), pdImage.getWidth()*scale, pdImage.getHeight()*scale);

        int fontSize = 12;
        float center = page.getMediaBox().getWidth() / 2;
        PDType1Font helvericaBoldFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDType1Font helvericaFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        float titleWidth = helvericaBoldFont.getStringWidth(title) / 1000 * fontSize;
        contentStream.beginText();
        contentStream.setFont(helvericaBoldFont, fontSize);
        contentStream.newLineAtOffset(center - (titleWidth / 2), yStartNewPage - (pdImage.getHeight() * scale)/2 + 20);
        contentStream.showText(title);
        contentStream.endText();

        String report = messages.getString(reportType);
        float reportWidth = helvericaBoldFont.getStringWidth(report) / 1000 * fontSize;
        contentStream.beginText();
        contentStream.setFont(helvericaBoldFont, fontSize);
        contentStream.newLineAtOffset(center - (reportWidth / 2), yStartNewPage - (pdImage.getHeight() * scale)/2);
        contentStream.showText(report);
        contentStream.endText();

        int correctAsFontSize = 10;
        String correct = messages.getString("correct_as_of") + " " + now;
        float correctWidth = helvericaFont.getStringWidth(correct) / 1000 * correctAsFontSize;
        contentStream.beginText();
        contentStream.setFont(helvericaFont, correctAsFontSize);
        contentStream.newLineAtOffset(center - (correctWidth / 2), yStartNewPage - (pdImage.getHeight() * scale)/2 - 50);
        contentStream.showText(correct);
        contentStream.endText();

        contentStream.close();
    }

    private void fillFooter(PDDocument document, Date now, float margin) throws IOException {
        PDPageTree pages = document.getDocumentCatalog().getPages();
        int numberOfPages = pages.getCount();
        int count = 1;
        Iterator<PDPage> iterator = pages.iterator();
        PDFont footerFont = new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);
        while (iterator.hasNext()) {
            PDPage pdPage = iterator.next();
            String text = messages.getString("page") + " " + count + " " + messages.getString("of") + " " + numberOfPages;
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
        PDType1Font helvericaFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        cell.setFont(helvericaFont);
        cell.setFontSize(10);
        if (fastestInSection) {
            cell.setFillColor(Color.YELLOW);
        }
        return cell;
    }

    private void createPdfReport(File resultsFile, String title, File logoFile, String reportType, Date now, List<EntriesForEventPositions> entriesByEventPositions) throws ServiceException, IOException, URISyntaxException {

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
        document.addPage(page);

        PDImageXObject pdImage = getLogoImage(logoFile, document);

        float margin = 10;
        float scale = 0.15f;
        float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);

        fillHeader(document, page, pdImage, title, scale, yStartNewPage, reportType, now);

        float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
        boolean drawContent = true;
        float yStart = yStartNewPage - (pdImage.getHeight() * scale) - 10;
        float bottomMargin = 20;

        boolean firstTable = true;
        PDType1Font helvericaFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        for (EntriesForEventPositions e: entriesByEventPositions) {
            if (firstTable) {
                firstTable = false;
            } else {
                page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
                document.addPage(page);
                yStart = yStartNewPage - 10;
            }
            BaseTable table = new BaseTable(yStart, yStartNewPage, bottomMargin, tableWidth, margin, document, page, true, drawContent);

            Row<PDPage> positionsRow = table.createRow(15f);
            String positions = null;
            if (e.getPositions().size() > 0) {
                positions = e.getPositions().get(0).getPosition().getName() + " " + messages.getString("to") + " " + e.getPositions().get(e.getPositions().size() - 1).getPosition().getName();
            }
            Cell<PDPage> cell = positionsRow.createCell(100, positions);
            cell.setFont(helvericaFont);
            cell.setFillColor(Color.BLACK);
            cell.setTextColor(Color.WHITE);
            table.addHeaderRow(positionsRow);

            List<Entry> entries = e.getEntries();
            fillResults(entries, table, times);
            yStart = table.draw();
        }

        fillFooter(document, now, margin);

        document.save(resultsFile);
        document.close();
    }

    private void addHeaderCell(int width, String text, Row<PDPage> headerRow)  {
        Cell<PDPage> cell = headerRow.createCell(width, text);
        PDType1Font helvericaFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        cell.setFont(helvericaFont);
        cell.setFillColor(Color.BLACK);
        cell.setTextColor(Color.WHITE);
    }

    private void fillResults(List<Entry> entries, BaseTable table, List<EntryPositionTime> times) {
        // need to scale based on the number of positions all has to add to 100
        List<EventPosition> positions = entries.get(0).getEvent().getPositions();
        int numberOfPositions = positions.size() - 1;
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

        Row<PDPage> headerRow = table.createRow(15f);
        addHeaderCell(numberWidth, messages.getString("number"), headerRow);
        addHeaderCell(eventWidth, messages.getString("event"), headerRow);
        addHeaderCell(clubWidth, messages.getString("club"), headerRow);
        addHeaderCell(crewWidth, messages.getString("details"), headerRow);
        if (numberOfPositions > 0) {
            for (int i = 1; i <= numberOfPositions; i++) {
                Position position = positions.get(i).getPosition();
                addHeaderCell(positionWidth, position.getName(), headerRow);
            }
        }
        addHeaderCell(commentWidth, messages.getString("comment"), headerRow);
        table.addHeaderRow(headerRow);

        for (int x = 0; x < entries.size(); x++) {
            Entry entry = entries.get(x);
            Row<PDPage> row = table.createRow(10f);
            boolean fastestInSection = false;
            if (x == 0) {
                fastestInSection = true;
            } else if (x >= 1) {
                Entry prevEntry = entries.get(x - 1);
                if (prevEntry.getEvent().getId() != entry.getEvent().getId()) {
                    fastestInSection = true;
                }
            }

            boolean disqualified = isEntryDisqualified(entry);
            Cell<PDPage> number = createCellForRow(row, numberWidth, entry.getNumber().toString(), fastestInSection);
            Cell<PDPage> event = createCellForRow(row, eventWidth, entry.getEvent().getName(), fastestInSection);
            Cell<PDPage> club = createCellForRow(row, clubWidth, getClubsForEntry(entry), fastestInSection);
            Cell<PDPage> crew = createCellForRow(row, crewWidth, entry.getCrew(), fastestInSection);
            if (entry.getEvent().getPositions().size() > 0) {
                List<EntryPositionTime> entryTimes = getPositionTimesForEntryByPosition(entry, times);
                Position startingPosition = entry.getEvent().getPositions().get(0).getPosition();
                Timestamp startTimestamp = getTimeForPosition(startingPosition, entryTimes);
                Instant startingPositionInstant = null;
                if (startTimestamp != null) {
                    startingPositionInstant = startTimestamp.toInstant();
                }
                for (int i = 1; i < entry.getEvent().getPositions().size(); i++) {
                    Position position = entry.getEvent().getPositions().get(i).getPosition();
                    Timestamp positionTimestamp = getTimeForPosition(position, entryTimes);
                    if (!disqualified && positionTimestamp != null && startTimestamp != null) {
                        Instant positionInstant = positionTimestamp.toInstant();
                        if (i == entry.getEvent().getPositions().size() - 1) {
                            positionInstant = addPenaltiesToTimeForEntry(positionInstant, entry);
                        }
                        Duration timeToPosition = Duration.between(startingPositionInstant, positionInstant);
                        String time = getTimeForDuration(timeToPosition);
                        createCellForRow(row, positionWidth, time, fastestInSection);
                    } else {
                        createCellForRow(row, positionWidth, "", fastestInSection);
                    }
                }
            }
            if (disqualified) {
                createCellForRow(row, commentWidth, messages.getString("disqualified"), false);
            } else if (hasEntryPenalties(entry)) {
                createCellForRow(row, commentWidth, messages.getString("penalty"), fastestInSection);
            } else {
                createCellForRow(row, commentWidth, "", fastestInSection);
            }
        }
    }

    private Instant addPenaltiesToTimeForEntry(Instant time, Entry entry) {
        List<Penalty> entryPenalties = getPenaltiesForEntry(entry);
        if (entryPenalties.size() > 0) {
            for (Penalty penalty: entryPenalties) {
                time = time.plusMillis(penalty.getTime().getTime());
            }
        }
        return time;
    }

    private boolean isEntryDisqualified(Entry entry) {
        for (Disqualification disqualification: disqualifications) {
            if (disqualification.getEntry().getId() == entry.getId()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasEntryPenalties(Entry entry) {
        for (Penalty penalty: penalties) {
            if (penalty.getEntry().getId() == entry.getId()) {
                return true;
            }
        }
        return false;
    }

    private List<Penalty> getPenaltiesForEntry(Entry entry) {
        List<Penalty> penaltiesForEntry = new ArrayList<>();
        for (Penalty penalty: penalties) {
            if (penalty.getEntry().getId() == entry.getId()) {
                penaltiesForEntry.add(penalty);
            }
        }
        return penaltiesForEntry;
    }

    private String getTimeForDuration(Duration duration) {
        String time = "";
        long hours = duration.toHours();
        if (hours < 10) {
            time = time + "0";
        }
        time = time + hours + ":";
        long minutes = duration.minusHours(hours).toMinutes();
        if (minutes < 10) {
            time = time + "0";
        }
        time = time + minutes + ":";
        long seconds = duration.minusHours(hours).minusMinutes(minutes).toMillis()/1000;
        if (seconds < 10) {
            time = time + "0";
        }
        time = time + seconds;

        return time;
    }

    private Timestamp getTimeForPosition(Position position, List<EntryPositionTime> entryTimes) {
        for (EntryPositionTime entryPositionTime: entryTimes) {
            if (entryPositionTime.getPosition().getId() == position.getId()) {
                return entryPositionTime.getTime();
            }
        }
        return null;
    }

    private List<EntryPositionTime> getPositionTimesForEntryByPosition(Entry entry, List<EntryPositionTime> times) {
        List<EntryPositionTime> entryPositionTimes = new ArrayList<>();
        for (EntryPositionTime time: times) {
            if (time.getEntry().getId() == entry.getId()) {
                entryPositionTimes.add(time);
            }
        }
        List<EventPosition> eventPositions = entry.getEvent().getPositions();

        entryPositionTimes.sort(new Comparator<EntryPositionTime>() {
            @Override
            public int compare(EntryPositionTime t1, EntryPositionTime t2) {
                Integer t1PositionOrder = 0;
                Integer t2PositionOrder = 0;
                for (EventPosition eventPosition : eventPositions) {
                    if (eventPosition.getPosition().getId() == t1.getPosition().getId()) {
                        t1PositionOrder = eventPosition.getPositionOrder();
                    }
                    if (eventPosition.getPosition().getId() == t2.getPosition().getId()) {
                        t2PositionOrder = eventPosition.getPositionOrder();
                    }
                }

                return t2PositionOrder - t1PositionOrder;
            }
        });

        return entryPositionTimes;
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
