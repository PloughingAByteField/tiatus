import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/interval';
import { TranslateService } from '@ngx-translate/core';

import { Position } from '../../positions/position.model';
import { Race } from '../../races/race.model';
import { Club } from '../../clubs/club.model';
import { Entry } from '../../entries/entry.model';
import { Event } from '../../events/event.model';
import { Penalty } from '../../penalties/penalty.model';
import { Disqualification } from '../../disqualification/disqualification.model';
import { PositionTime, convertFromTimeStamp } from '../../times/postion-time.model';
import { EntryTime } from '../../times/entry-time.model';

import { PositionsService } from '../../positions/positions.service';
import { RacesService } from '../../races/races.service';
import { EntryTimesService } from '../../times/entry-times.service';
import { PenaltiesService } from '../../penalties/penalties.service';
import { DisqualificationService } from '../../disqualification/disqualification.service';
import { EventsService } from '../../events/events.service';
import { ClubsService } from '../../clubs/clubs.service';

@Component({
    selector: 'race-results',
    styleUrls: [ './race-results.component.css' ],
    templateUrl: './race-results.component.html',
    providers: [ EntryTimesService ]
})
export class RaceResultsComponent implements OnInit, OnDestroy {
    public page: number = 1;
    public itemsPerPage: number = 10;
    public positions: Position[];
    public displayablePositions: Position[];
    public reverseNumberSort = false;
    public reverseAdjustedTimeSort = false;
    public numberFilter: string = '';
    public clubFilter: string = '';
    public eventFilter: string = '';
    public filteredEntryTimes: EntryTime[];

    private pollingPeriod: number = 20000;
    private raceId: number = 0;
    private clubs: Club[] = new Array<Club>();
    private events: Event[] = new Array<Event>();
    private races: Race[];
    private race: Race;
    private entryTimes: EntryTime[];
    private disqualifiedText: string;
    private penaltyText: string;
    private penalties: Penalty[];
    private disqualifications: Disqualification[];
    private polling: any;

    constructor(
        private route: ActivatedRoute,
        private translate: TranslateService,
        private racesService: RacesService,
        private positionsService: PositionsService,
        private entryTimesService: EntryTimesService,
        private clubsService: ClubsService,
        private eventsService: EventsService,
        private penaltiesService: PenaltiesService,
        private disqualificationService: DisqualificationService
    ) {}

    public ngOnInit() {
        this.positionsService.getPositions()
            .subscribe((positions: Position[]) => {
                this.positions = positions;
                this.displayablePositions = positions.slice();
                this.displayablePositions.shift();
        });

        this.racesService.getRaces().subscribe((races: Race[]) => {
            this.races = races;
            if (this.raceId) {
                this.setRaceForRaceId(this.raceId);
            }
        });

        this.penaltiesService.getPenalties().subscribe((penalties: Penalty[]) => {
            this.penalties = penalties;
        });

        this.clubsService.getClubs().subscribe((clubs: Club[]) => {
            this.clubs = clubs;
        });

        this.eventsService.getEvents().subscribe((events: Event[]) => {
            this.events = events;
        });

        this.disqualificationService.getDisqualifications()
            .subscribe((disqualifications: Disqualification[]) => {
                this.disqualifications = disqualifications;
            });

        this.route.params.subscribe((params: Params) => {
            this.raceId = +params['raceId'];
            this.setRaceForRaceId(this.raceId);
            this.numberFilter = '';
            this.clubFilter = '';
            this.eventFilter = '';
        });

        this.translate.get('DISQUALIFIED').subscribe((res: string) => {
            this.disqualifiedText = res;
        });
        this.translate.get('PENALTY').subscribe((res: string) => {
            this.penaltyText = res;
        });

        this.polling = Observable.interval(this.pollingPeriod).map(() => {
            console.log('polling');
            this.racesService.refresh();
            this.positionsService.refresh();
            this.penaltiesService.refresh();
            this.disqualificationService.refresh();
            if (this.race) {
                this.entryTimesService.refreshForRace(this.race);
            }
        }).subscribe();
    }

    public ngOnDestroy() {
        console.log('bye');
        this.polling.unsubscribe();
    }

    public onKey(value: string, field: string) {
        if (field === 'number') {
            this.numberFilter = value;
        }
        if (field === 'club') {
            this.clubFilter = value;
        }
        if (field === 'event') {
            this.eventFilter = value;
        }
        this.filteredEntryTimes = this.filterEntries();
    }

    public sortByNumber(direction: string): void {
        this.reverseNumberSort = !this.reverseNumberSort;
    }

    public sortByAdjustedTime(direction: string): void {
        this.reverseNumberSort = !this.reverseNumberSort;
    }

    public getTimeForEntry(entryTime: EntryTime, position: Position): string {
        if (this.isDisqualified(entryTime)) {
            return null;
        }
        if (entryTime.times.length > 0) {
            let actualStartPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.startPoint === true)
                .shift();
            let eventStartPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.position
                    === entryTime.entry.event)
                .shift();
            let time: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.position === position.id)
                .shift();
            if (time && actualStartPoint) {
                return convertFromTimeStamp(time.time - actualStartPoint.time);
            }
        }
        return null;
    }

    public getAdjustedTimeForEntry(entryTime: EntryTime): string {
        if (this.isDisqualified(entryTime)) {
            return null;
        }
        if (entryTime.times.length > 0) {
            let actualStartPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.startPoint === true)
                .shift();
            let eventStartPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.position
                    === entryTime.entry.event)
                .shift();
            let eventFinishPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.position
                     === entryTime.entry.event)
                .shift();
            if (eventFinishPoint && actualStartPoint) {
                // apply penalties
                let penalties: number = this.getPenaltiesForEntry(entryTime);
                return convertFromTimeStamp(
                    eventFinishPoint.time - actualStartPoint.time + penalties);
            }
        }
        return null;
    }

    public getCommentsForEntry(entryTime: EntryTime): string {
        if (this.isDisqualified(entryTime)) {
           return this.disqualifiedText;
        }
        if (this.getPenaltiesForEntry(entryTime) > 0) {
           return this.penaltyText;
        }
        return null;
    }

    public getEventNameForEntry(entry: Entry): string {
        let eventName: string;
        let event: Event = this.getEventForId(entry.event);
        if (event) {
            eventName = event.name;
        }
        return eventName;
    }

    public getClubNamesForEntry(entry: Entry): string {
        let clubs: string;
        for (let clubId of entry.clubs) {
            let club: Club = this.getClubForId(clubId);
            if (club) {
                if (!clubs) {
                    clubs = club.clubName;
                } else {
                    clubs = clubs.concat(' / ');
                    clubs = clubs.concat(club.clubName);
                }
            }
        }
        return clubs;
    }

    private getEventForId(eventId: number): Event {
        for (let event of this.events) {
            if (event.id === eventId) {
                return event;
            }
        }
        return null;
    }

    private getClubForId(clubId: number): Club {
        for (let club of this.clubs) {
            if (club.id === clubId) {
                return club;
            }
        }
        return null;
    }

    private isDisqualified(entry: EntryTime): boolean {
        if (this.disqualifications) {
            let disqualifed: Disqualification = this.disqualifications
                .filter((disqualification: Disqualification) =>
                    disqualification.entry === entry.entry.id).shift();
            if (disqualifed) {
                return true;
            }
        }
        return false;
    }

    private getPenaltiesForEntry(entryTime: EntryTime): number {
        if (this.isDisqualified(entryTime)) {
            return 0;
        }

        return this.getTimeForPenaltes(entryTime);
    }

    private getTimeForPenaltes(entryTime: EntryTime): number {
        let penalties: Penalty[];
        if (this.penalties) {
            penalties = this.penalties.filter((penalty: Penalty) =>
                penalty.entry === entryTime.entry.id);
        }

        if (penalties) {
            let total: number = 0;
            penalties.map((penalty: Penalty) => total += penalty.time);
            return total;
        }
        return 0;
    }

    private getTime(entryTime: EntryTime): number {
        if (entryTime.times.length > 0) {
            let actualStartPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.startPoint === true)
                .shift();
            let eventStartPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.position
                    === entryTime.entry.event)
                .shift();
            let eventFinishPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.position
                        === entryTime.entry.event)
                .shift();
            if (eventFinishPoint && actualStartPoint) {
                return eventFinishPoint.time - actualStartPoint.time;
            }
        }

        return 0;
    }
    private getTimesForRace(race: Race): void {
        if (race) {
            console.log('Get times for race ' + race.id);
            this.entryTimesService.getEntriesForRace(this.race)
                .subscribe((data: EntryTime[]) => {
                    console.log(data);
                    this.entryTimes = data;
                    this.filteredEntryTimes = this.entryTimes;
            });
        }
    }

    private setRaceForRaceId(raceId: number): void {
        if (this.races) {
            this.race = this.racesService.getRaceForId(this.raceId);
            this.getTimesForRace(this.race);
        }
    }

    private filterEntries(): EntryTime[] {
        let filteredEntries: EntryTime[] = this.entryTimes;
        if (this.numberFilter) {
            filteredEntries = this.filterNumbers(filteredEntries, this.numberFilter);
        }
        if (this.clubFilter) {
            filteredEntries = this.filterClubs(filteredEntries, this.clubFilter);
        }
        if (this.eventFilter) {
            filteredEntries = this.filterEvents(filteredEntries, this.eventFilter);
        }

        return filteredEntries;
    }

    private filterNumbers(entryTimes: EntryTime[], value: string): EntryTime[] {
        return entryTimes.filter((entryTime: EntryTime) =>
            entryTime.entry.number.toString().includes(value));
    }

    private filterClubs(entryTimes: EntryTime[], value: string): EntryTime[] {
        return entryTimes.filter((entryTime: EntryTime) => {
            for (let clubId of entryTime.entry.clubs) {
                let club: Club = this.getClubForId(clubId);
                if (club) {
                    if (club.clubName.includes(value)) {
                        return entryTime;
                    }
                }
            }
        });

    }

    private filterEvents(entryTimes: EntryTime[], value: string): EntryTime[] {
        return entryTimes.filter((entryTime: EntryTime) => {
            let event: Event = this.getEventForId(entryTime.entry.event);
            if (event) {
                if (event.name.includes(value)) {
                    return entryTime;
                }
            }
        });
    }
}
