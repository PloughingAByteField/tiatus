import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { TimingTimesService } from '../times/times.service';
import { RacesService } from '../../races/races.service';

import { Club } from '../../clubs/club.model';
import { Entry } from '../../entries/entry.model';
import { Event } from '../../events/event.model';
import { Position } from '../../positions/position.model';
import { Race } from '../../races/race.model';
import { PositionTime, convertFromTimeStamp, convertToTimeStamp }
    from '../../times/postion-time.model';
import { EntryTime } from '../../times/entry-time.model';

import { TimingPositionService } from '../times/timing-position.service';
import { TimingEntryTimesService } from '../times/entry-times.service';
import { EventsService } from '../../events/events.service';
import { ClubsService } from '../../clubs/clubs.service';

@Component({
    styleUrls: [ './timing-entry.component.css' ],
    templateUrl: './timing-entry.component.html',
    providers: [ TimingTimesService, TimingEntryTimesService ]
})
export class TimingEntryComponent implements OnInit {
    public raceEntries: Entry[];
    public page: number = 1;
    public itemsPerPage: number = 10;
    public filteredEntryTimes: EntryTime[];
    public numberFilter: string = '';
    public clubFilter: string = '';
    public eventFilter: string = '';
    public reverseTimeSort = false;
    public reverseNumberSort = false;
    public reverseSyncedSort = false;

    private position: Position;
    private entryTimes: EntryTime[];
    private times: PositionTime[];
    private raceId: number = 0;
    private races: Race[];
    private clubs: Club[] = new Array<Club>();
    private events: Event[] = new Array<Event>();
    private race: Race;

    constructor(
        private route: ActivatedRoute,
        private timingPositionService: TimingPositionService,
        private timesService: TimingTimesService,
        private racesService: RacesService,
        private clubsService: ClubsService,
        private eventsService: EventsService,
        private entryTimesService: TimingEntryTimesService
    ) {}

    public ngOnInit() {
        this.racesService.getRaces().subscribe((races: Race[]) => {
            this.races = races;
            if (this.raceId) {
                this.race = this.racesService.getRaceForId(this.raceId);
            }
            this.getTimesForRace(this.raceId);
        });

        this.timingPositionService.getPosition.subscribe((position: Position) => {
            if (position) {
                this.position = position;
                this.getTimesForRace(this.raceId);
            }
        });

        this.clubsService.getClubs().subscribe((clubs: Club[]) => {
            this.clubs = clubs;
        });

        this.eventsService.getEvents().subscribe((events: Event[]) => {
            this.events = events;
        });

        this.route.params.subscribe((params: Params) => {
            this.raceId = +params['raceId'];
            if (this.races) {
                this.race = this.racesService.getRaceForId(this.raceId);
            }
            this.getTimesForRace(this.raceId);
            this.numberFilter = '';
            this.clubFilter = '';
            this.eventFilter = '';
        });

    }

    public enterTime(value: string, entryTime: EntryTime) {
        if (value) {
            let timeStamp: number = convertToTimeStamp(value);
            let positonTime: PositionTime = this.getPositionTimeForPosition(entryTime);
            this.entryTimesService
                .addTimeForPosition(this.position, timeStamp, positonTime, entryTime.entry);
        }
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
        this.filteredEntryTimes.sort((e1, e2) => {
            let order: number;
            if (e1.entry.number < e2.entry.number) {
                order = -1;

            } else if (e1.entry.number === e2.entry.number) {
                order = 0;

            } else if (e1.entry.number > e2.entry.number) {
                order = 1;
            }

            if (direction === 'up') {
                order = order * -1;
            }

            return order;
        });
    }

    public sortByTime(direction: string): void {
        this.reverseTimeSort = !this.reverseTimeSort;
        this.filteredEntryTimes.sort((a, b) => {
            let apt: PositionTime = this.getPositionTimeForPosition(a);
            let bpt: PositionTime = this.getPositionTimeForPosition(b);
            if (apt.time === undefined) {
                return 1;
            } else if (bpt.time === undefined) {
                return -1;
            } else if (apt.time === bpt.time) {
                return 0;
            } else if (direction === 'up') {
                return apt.time < bpt.time ? -1 : 1;
            } else if (direction !== 'up') {
                return apt.time < bpt.time ? 1 : -1;
            }
         });
    }

    public sortBySynced(direction: string): void {
        this.reverseSyncedSort = !this.reverseSyncedSort;
        this.filteredEntryTimes.sort((a, b) => {
            let apt: PositionTime = this.getPositionTimeForPosition(a);
            let bpt: PositionTime = this.getPositionTimeForPosition(b);
            if (apt.synced === undefined) {
                return 1;
            } else if (bpt.synced === undefined) {
                return -1;
            } else if (apt.synced === bpt.synced) {
                return 0;
            } else if (direction === 'up') {
                return apt.synced < bpt.synced ? -1 : 1;
            } else if (direction !== 'up') {
                return apt.synced < bpt.synced ? 1 : -1;
            }
         });
    }

    public convertFromTimeStamp(entryTime: EntryTime): string {
        let timeForPosition: PositionTime = this.getPositionTimeForPosition(entryTime);
        if (timeForPosition) {
            return convertFromTimeStamp(+timeForPosition.time);
        }
        return null;
    };

    public getSyncedForPosition(entryTime: EntryTime): boolean {
        let timeForPosition: PositionTime = this.getPositionTimeForPosition(entryTime);
        if (timeForPosition) {
            return timeForPosition.synced;
        }
        return false;
    };

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

    private getPositionTimeForPosition(entryTime: EntryTime): PositionTime {
        return entryTime.times
            .filter((pt: PositionTime) => pt.position === this.position.id).shift();
    }

    private getTimesForRace(raceId): void {
        if (this.position && this.race) {
            console.log('get entries');
            this.entryTimesService.getTimesForPositionInRace(this.position, this.race)
            // this.entryTimesService.getEntriesForRace(this.race)
                .subscribe((data: EntryTime[]) => {
                console.log(data);
                this.entryTimes = data;
                this.filteredEntryTimes = this.entryTimes;
                console.log(data);
            });
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
