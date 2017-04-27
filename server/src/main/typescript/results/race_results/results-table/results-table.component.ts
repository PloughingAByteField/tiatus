import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';

import { TranslateService } from '@ngx-translate/core';

import { Position } from '../../../positions/position.model';
import { Race } from '../../../races/race.model';
import { Club } from '../../../clubs/club.model';
import { Entry } from '../../../entries/entry.model';
import { Event } from '../../../events/event.model';
import { Penalty } from '../../../penalties/penalty.model';
import { Disqualification } from '../../../disqualification/disqualification.model';
import { EntryTime } from '../../../times/entry-time.model';
import { PositionTime, convertFromTimeStamp , convertToTimeStamp } from '../../../times/postion-time.model';
import { EventPosition } from '../../../events/event-positions.model';

import { ResultsPositionsService } from '../../positions/positions.service';
import { ResultsRacesService } from '../../races/races.service';
import { EntryTimesService } from '../../../times/entry-times.service';
import { ResultsPenaltiesService } from '../../penalties/penalties.service';
import { ResultsDisqualificationService } from '../../disqualification/disqualification.service';
import { ResultsEventsService } from '../../events/events.service';
import { ResultsClubsService } from '../../clubs/clubs.service';

@Component({
    selector: 'race-results-table',
    styleUrls: [ './results-table.component.css' ],
    templateUrl: './results-table.component.html'
})
export class RaceResultsTableComponent implements OnInit, OnDestroy {

    public page: number = 1;
    public itemsPerPage: number = 10;

    public start: Position;
    public finish: Position;
    public race: Race;
    public filteredEntryTimes: EntryTime[];
    public numberFilter: string = '';
    public clubFilter: string = '';
    public eventFilter: string = '';
    public reverseNumberSort = false;
    public reverseAdjustedTimeSort = false;

    private pollingPeriod: number = 30000;
    private disqualifications: Disqualification[];
    private penalties: Penalty[];
    private positions: Position[];
    private events: Event[];
    private clubs: Club[];
    private races: Race[];
    private entryTimes: EntryTime[];
    private entryTimesForPositions: EntryTime[];

    private disqualifiedText: string;
    private penaltyText: string;

    private raceId: number;
    private from: string;
    private to: string;
    private positionsForTable: Position[];

    private positionsSubscription: Subscription;
    private parentRoutesSubscription: Subscription;
    private routesSubscription: Subscription;
    private racesSubscription: Subscription;
    private penaltiesSubscription: Subscription;
    private clubsSubscription: Subscription;
    private eventsSubscription: Subscription;
    private raceTimesSubscription: Subscription;
    private disqualificationsSubscription: Subscription;
    private translateDisqualifiedSubscription: Subscription;
    private translatePenaltySubscription: Subscription;
    private polling: Subscription;

     constructor(
        private route: ActivatedRoute,
        private translate: TranslateService,
        private racesService: ResultsRacesService,
        private entryTimesService: EntryTimesService,
        private clubsService: ResultsClubsService,
        private eventsService: ResultsEventsService,
        private penaltiesService: ResultsPenaltiesService,
        private disqualificationService: ResultsDisqualificationService,
        private positionsService: ResultsPositionsService) {}

    public ngOnInit() {
        this.parentRoutesSubscription = this.route.parent.params.subscribe((params: Params) => {
            const raceId: number = +params['raceId'];
            if (raceId) {
                this.raceId = +params['raceId'];
                if (this.races && this.races.length > 0) {
                    this.setRace(this.raceId);
                }
            }
        });

        this.routesSubscription = this.route.params.subscribe((params: Params) => {
            const from: string = params['from'];
            if (from) {
                this.from = from;
                if (this.positions && this.positions.length > 0) {
                    this.start = this.positionsService.getPositionForName(this.from);
                }
            }
            const to: string = params['to'];
            if (to) {
                this.to = to;
                if (this.positions && this.positions.length > 0) {
                    this.finish = this.positionsService.getPositionForName(this.to);
                }
            }

            this.getTimesForRace(this.race);
        });

        this.positionsSubscription = this.positionsService.getPositions()
            .subscribe((positions: Position[]) => {
                this.positions = positions;
                if (this.positions && this.positions.length > 0) {
                    if (this.from) {
                        this.start = this.positionsService.getPositionForName(this.from);
                    }
                    if (this.to) {
                        this.finish = this.positionsService.getPositionForName(this.to);
                    }
                }
                this.getTimesForRace(this.race);
        });

        this.racesSubscription = this.racesService.getRaces().subscribe((races: Race[]) => {
            this.races = races;
            if (this.raceId && races.length > 0) {
                this.setRace(this.raceId);
            }
        });

        this.penaltiesSubscription =
            this.penaltiesService.getPenalties().subscribe((penalties: Penalty[]) => {
            this.penalties = penalties;
            this.getTimesForRace(this.race);
        });

        this.clubsSubscription = this.clubsService.getClubs().subscribe((clubs: Club[]) => {
            this.clubs = clubs;
            this.getTimesForRace(this.race);
        });

        this.eventsSubscription = this.eventsService.getEvents().subscribe((events: Event[]) => {
            this.events = events;
            this.getTimesForRace(this.race);
        });

        this.disqualificationsSubscription = this.disqualificationService.getDisqualifications()
            .subscribe((disqualifications: Disqualification[]) => {
                this.disqualifications = disqualifications;
                this.getTimesForRace(this.race);
        });

        this.translateDisqualifiedSubscription =
            this.translate.get('DISQUALIFIED').subscribe((res: string) => {
            this.disqualifiedText = res;
        });

        this.translatePenaltySubscription =
            this.translate.get('PENALTY').subscribe((res: string) => {
            this.penaltyText = res;
        });

        this.polling = Observable.interval(this.pollingPeriod).map(() => {
            this.racesService.refresh();
            this.positionsService.refresh();
            this.clubsService.refresh();
            this.eventsService.refresh();
            this.penaltiesService.refresh();
            this.disqualificationService.refresh();
            if (this.race) {
                this.entryTimesService.refreshForRace(this.race);
            }
        }).subscribe();
    }

    public ngOnDestroy() {
        if (this.raceTimesSubscription) {
            this.raceTimesSubscription.unsubscribe();
        }
        this.parentRoutesSubscription.unsubscribe();
        this.routesSubscription.unsubscribe();
        this.positionsSubscription.unsubscribe();
        this.racesSubscription.unsubscribe();
        this.penaltiesSubscription.unsubscribe();
        this.clubsSubscription.unsubscribe();
        this.eventsSubscription.unsubscribe();
        this.disqualificationsSubscription.unsubscribe();
        this.translateDisqualifiedSubscription.unsubscribe();
        this.translatePenaltySubscription.unsubscribe();
        this.polling.unsubscribe();
    }

    public getPositions(): Position[] {
        if (this.positionsForTable) {
            return this.positionsForTable;
        }
        if (this.entryTimesForPositions && this.entryTimesForPositions.length > 0) {
            const event: Event =
                this.eventsService.getEventForId(this.entryTimesForPositions[0].entry.event);
            const eventPositions: EventPosition[] = event.positions;
            this.positionsForTable = new Array<Position>();
            eventPositions.map((ev: EventPosition) => {
                this.positionsForTable.push(this.positionsService.getPositionForId(ev.position));
            });
            return this.positionsForTable;
        }
        return null;
    }

    public sortByAdjustedTime(direction: string): void {
        this.reverseAdjustedTimeSort = !this.reverseAdjustedTimeSort;
        this.filteredEntryTimes.sort((a, b) => {
            const adjustedA: string = this.getAdjustedTimeForEntry(a);
            const adjustedB: string = this.getAdjustedTimeForEntry(b);
            if (adjustedA === null) {
                return 1;
            } else if (adjustedB === null) {
                return -1;
            } else if (adjustedA === adjustedB) {
                return 0;
            } else if (direction === 'up') {
                const aTime: number = convertToTimeStamp(adjustedA);
                const bTime: number = convertToTimeStamp(adjustedB);
                return aTime < bTime ? -1 : 1;
            } else if (direction !== 'up') {
                const aTime: number = convertToTimeStamp(adjustedA);
                const bTime: number = convertToTimeStamp(adjustedB);
                return aTime < bTime ? 1 : -1;
            }
         });
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

    public getTimeForEntry(entryTime: EntryTime, position: Position): string {
        if (this.isDisqualified(entryTime)) {
            return null;
        }

        if (this.positionsForTable && entryTime.times.length > 0) {
            const actualStartPoint: PositionTime = entryTime.times[0];
            const event: Event = this.eventsService.getEventForId(entryTime.entry.event);
            const eventPositions: EventPosition[] = event.positions;
            let time: PositionTime;
            for (const positionTime of entryTime.times) {
                if (positionTime.position === position.id) {
                    time = positionTime;
                    break;
                }
            }
            if (time && actualStartPoint) {
                return convertFromTimeStamp(time.time - actualStartPoint.time);
            }
        }
        return null;
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

    public getAdjustedTimeForEntry(entryTime: EntryTime): string {
        if (this.isDisqualified(entryTime)) {
            return null;
        }
        if (entryTime.times && entryTime.times.length > 0) {
            const actualStartPoint: PositionTime = entryTime.times[0];
            const event: Event = this.eventsService.getEventForId(entryTime.entry.event);
            if (event.positions.length > 0) {
                const eventPositions: EventPosition[] = event.positions;
                let eventFinishPoint: PositionTime;
                if (entryTime.times[entryTime.times.length - 1].position
                    === eventPositions[eventPositions.length - 1].position) {
                    eventFinishPoint = entryTime.times[entryTime.times.length - 1];
                }
                if (eventFinishPoint && actualStartPoint) {
                    // apply penalties
                    const penalties: number = this.getPenaltiesForEntry(entryTime);
                    return convertFromTimeStamp(
                        eventFinishPoint.time - actualStartPoint.time + penalties);
                }
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
        const event: Event = this.eventsService.getEventForId(entry.event);
        if (event) {
            eventName = event.name;
        }
        return eventName;
    }

    public getClubNamesForEntry(entry: Entry): string {
        let clubs: string;
        for (const clubId of entry.clubs) {
            const club: Club = this.clubsService.getClubForId(clubId);
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

    private setRace(raceId: number): void {
        this.race = this.racesService.getRaceForId(raceId);
        this.getTimesForRace(this.race);
    }

    private getPositionOrder(entry: Entry, positonId: number): number {
        const event: Event = this.eventsService.getEventForId(entry.event);
        const eventPositions: EventPosition[] = event.positions;
        for (const ep of eventPositions) {
            if (ep.position === positonId) {
                return ep.positionOrder;
            }
        }
        return -1;
    }

    private sortTimesByEntryPositionOrder(times: EntryTime[]): void {
        if (times && this.positionsForTable) {
            for (const entryTime of times) {
                entryTime.times.sort((t1: PositionTime, t2: PositionTime) => {
                    const t1PositionOrder: number =
                        this.getPositionOrder(entryTime.entry, t1.position);
                    const t2PositionOrder: number =
                        this.getPositionOrder(entryTime.entry, t2.position);
                    if (t1PositionOrder < t2PositionOrder) {
                        return -1;
                    }
                    if (t1PositionOrder > t2PositionOrder) {
                        return 1;
                    }
                    return 0;
                });
            }
        }
    }

    private getTimesForRace(race: Race): void {
        if (race) {
            this.raceTimesSubscription = this.entryTimesService.getEntriesForRace(race)
                .subscribe((data: EntryTime[]) => {
                    this.positionsForTable = undefined;
                    this.entryTimes = data;
                    this.entryTimesForPositions = data.filter((entrytime: EntryTime) => {
                        const event: Event = this.eventsService.getEventForId(entrytime.entry.event);
                        const eventPositions: EventPosition[] = event.positions;
                        if (eventPositions.length === 0) {
                            return false;
                        }
                        if (eventPositions[0].position === this.start.id
                            && eventPositions[eventPositions.length - 1].position
                                 === this.finish.id) {
                                return true;
                        }
                        return false;
                    });

                    this.sortTimesByEntryPositionOrder(this.entryTimesForPositions);
                    this.filteredEntryTimes = this.filterEntries();
            });
        }
    }

    private isDisqualified(entry: EntryTime): boolean {
        if (this.disqualifications) {
            const disqualifed: Disqualification = this.disqualifications
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

    private filterEntries(): EntryTime[] {
        let filteredEntries: EntryTime[] = this.entryTimesForPositions;
        if (filteredEntries.length > 0) {
            if (this.numberFilter) {
                filteredEntries = this.filterNumbers(filteredEntries, this.numberFilter);
            }
            if (this.clubFilter) {
                filteredEntries = this.filterClubs(filteredEntries, this.clubFilter);
            }
            if (this.eventFilter) {
                filteredEntries = this.filterEvents(filteredEntries, this.eventFilter);
            }
        }
        return filteredEntries;
    }

    private filterNumbers(entryTimes: EntryTime[], value: string): EntryTime[] {
        return entryTimes.filter((entryTime: EntryTime) =>
            entryTime.entry.number.toString().includes(value));
    }

    private filterClubs(entryTimes: EntryTime[], value: string): EntryTime[] {
        return entryTimes.filter((entryTime: EntryTime) => {
            for (const clubId of entryTime.entry.clubs) {
                const club: Club = this.clubsService.getClubForId(clubId);
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
            const event: Event = this.eventsService.getEventForId(entryTime.entry.event);
            if (event) {
                if (event.name.includes(value)) {
                    return entryTime;
                }
            }
        });
    }
}
