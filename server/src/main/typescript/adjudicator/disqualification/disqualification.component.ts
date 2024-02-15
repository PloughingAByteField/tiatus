import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Location } from '@angular/common';

import { Race } from '../../races/race.model';
import { Club } from '../../clubs/club.model';
import { Event } from '../../events/event.model';
import { Disqualification } from '../../disqualification/disqualification.model';
import { Entry } from '../../entries/entry.model';

import { EntriesService } from '../../entries/entries.service';
import { EventsService } from '../../events/events.service';
import { ClubsService } from '../../clubs/clubs.service';
import { AdjudicatorRacesService } from '../races/races.service';
import { AdjudicatorDisqualificationService } from './disqualification.service';

@Component({
    selector: 'disqualification',
    styleUrls: [ './disqualification.component.css' ],
    templateUrl: './disqualification.component.html'
})
export class DisqualificationComponent implements OnInit {

    @Input()
    public model: Disqualification = new Disqualification();
    public entry: Entry;

    private disqualifications: Disqualification[];
    private entries: Entry[];
    private entryId: number;
    private races: Race[];
    private events: Event[] = new Array<Event>();
    private clubs: Club[] = new Array<Club>();

    constructor(
        private route: ActivatedRoute,
        private entriesService: EntriesService,
        private racesService: AdjudicatorRacesService,
        private clubsService: ClubsService,
        private eventsService: EventsService,
        private disqualificationService: AdjudicatorDisqualificationService,
        private location: Location

    ) {}

    public ngOnInit() {
        this.entriesService.getEntries().subscribe((entries: Entry[]) => {
            this.entries = entries;
            if (this.entryId) {
                this.entry = this.getEntryForEntryId(this.entryId);
                this.model = this.getDisqualificationForEntry(this.entry);
            }
        });

        this.disqualificationService.getDisqualifications()
            .subscribe((disqualifications: Disqualification[]) => {
                this.disqualifications = disqualifications;
                if (this.entry) {
                    this.model = this.getDisqualificationForEntry(this.entry);
                }
        });

        this.route.params.subscribe((params: Params) => {
            this.entryId = +params['entryId'];
            this.entry = this.getEntryForEntryId(this.entryId);
            if (this.disqualifications && this.entry) {
                this.model = this.getDisqualificationForEntry(this.entry);
            }
        });

        this.clubsService.getClubs().subscribe((clubs: Club[]) => {
            this.clubs = clubs;
        });

        this.racesService.getRaces().subscribe((races: Race[]) => {
            this.races = races;
        });

        this.eventsService.getEvents().subscribe((events: Event[]) => {
            this.events = events;
        });
    }

    public isDisqualified(): boolean {
        if (this.model.id) {
            return true;
        }
        return false;
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

    public getRaceNameForEntry(entry: Entry): string {
        const race: Race = this.racesService.getRaceForId(entry.race);
        if (race) {
            return race.name;
        }
        return null;
    }

    public getEventNameForEntry(entry: Entry): string {
        const event: Event = this.eventsService.getEventForId(entry.event);
        if (event) {
            return event.name;
        }
        return null;
    }

    public addDisqualificationForEntry() {
        this.model.entry = this.entry.id;
        this.disqualificationService.disqualify(this.model);
        this.model = new Disqualification();
    }

    public removeDisqualificationForEntry(): void {
        this.disqualificationService.removeDisqualification(this.model);
        this.model = new Disqualification();
    }

    public updateDisqualificationForEntry(): void {
        this.disqualificationService.updateDisqualification(this.model);
    }

    public goBack(): void {
        this.location.back();
    }

    private getEntryForEntryId(entryId: number): Entry {
        let entry: Entry;
        if (this.entries) {
            entry = this.entries.filter((e: Entry) => e.id === entryId).shift();
        }
        return entry;
    }

    private getDisqualificationForEntry(entry: Entry): Disqualification {
        if (this.disqualifications) {
            const disqualification: Disqualification = this.disqualifications
                .filter((d: Disqualification) => d.entry === entry.id).shift();
            if (disqualification) {
                return disqualification;
            }
        }

        return this.model;
    }
}
