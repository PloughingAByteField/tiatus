import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';

import { Subscription } from 'rxjs/Subscription';

import { Club } from '../../../clubs/club.model';
import { Entry } from '../../../entries/entry.model';
import { Event } from '../../../events/event.model';
import { Race } from '../../../races/race.model';

import { AdminEventsService } from '../../events/events.service';
import { AdminClubsService } from '../../clubs/clubs.service';
import { AdminRacesService } from '../../races/races.service';

import { AdminEntriesService } from '../entries.service';

@Component({
  selector: 'entries-table',
  styleUrls: [ './entries-table.component.css' ],
  templateUrl: './entries-table.component.html'
})
export class EntriesTableComponent implements OnInit, OnDestroy {

  public entriesForRace: Entry[];
  public races: Race[] = new Array<Race>();
  public selectedRace: Race;
  public page: number = 1;
  public itemsPerPage: number = 10;

  private entries: Entry[];
  private clubs: Club[] = new Array<Club>();
  private events: Event[] = new Array<Event>();

  private entriesSubscription: Subscription;
  private eventsSubscription: Subscription;
  private clubsSubscription: Subscription;
  private racesSubscription: Subscription;

  constructor(
    private clubsService: AdminClubsService,
    private racesService: AdminRacesService,
    private eventsService: AdminEventsService,
    private entriesService: AdminEntriesService
   ) {}

  public ngOnInit() {
    this.entriesSubscription = this.entriesService.getEntries()
      .subscribe((entries: Entry[]) => {
        this.entries = entries;
        if (this.selectedRace) {
          this.changedRace(this.selectedRace);
        }
    });

    this.clubsSubscription = this.clubsService.getClubs().subscribe((clubs: Club[]) => {
        this.clubs = clubs;
        if (this.selectedRace) {
          this.changedRace(this.selectedRace);
        }
    });

    this.eventsSubscription = this.eventsService.getEvents().subscribe((events: Event[]) => {
        this.events = events;
        if (this.selectedRace) {
          this.changedRace(this.selectedRace);
        }
    });

    this.racesSubscription = this.racesService.getRaces().subscribe((races: Race[]) => {
        this.races = races;
        this.selectedRace = this.races[0];
        this.changedRace(this.selectedRace);
    });
  }

  public ngOnDestroy() {
    this.clubsSubscription.unsubscribe();
    this.eventsSubscription.unsubscribe();
    this.entriesSubscription.unsubscribe();
    this.racesSubscription.unsubscribe();
  }

  public removeEntry(entry: Entry): void {
    console.log('Remove ' + entry.id);
  }

  public changedRace(race: Race): void {
    console.log('changed race');
    console.log(race);
    this.entriesForRace = this.entries.filter((entry: Entry) => entry.race === race.id);
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
}
