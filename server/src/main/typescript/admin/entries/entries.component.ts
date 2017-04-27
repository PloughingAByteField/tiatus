import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';

import { Subscription } from 'rxjs/Subscription';

import { Club } from '../../clubs/club.model';
import { Entry } from '../../entries/entry.model';
import { Event } from '../../events/event.model';
import { Race } from '../../races/race.model';

import { AdminEventsService } from '../events/events.service';
import { AdminClubsService } from '../clubs/clubs.service';
import { AdminRacesService } from '../races/races.service';
import { SelectedRaceService } from '../races/selected-race.service';
import { AdminEntriesService } from './entries.service';

@Component({
  selector: 'entries',
  styleUrls: [ './entries.component.css' ],
  templateUrl: './entries.component.html'
})
export class EntriesComponent implements OnInit, OnDestroy {

  public entriesForRace: Entry[];
  public races: Race[] = new Array<Race>();
  public selectedRace: Race;
  public page: number = 1;
  public itemsPerPage: number = 10;
  public clubs: Club[] = new Array<Club>();
  private events: Event[] = new Array<Event>();
  private entries: Entry[];

  private eventsForRace: Event[] = new Array<Event>();

  private entriesSubscription: Subscription;
  private eventsSubscription: Subscription;
  private clubsSubscription: Subscription;
  private racesSubscription: Subscription;
  private selectedRaceSubscription: Subscription;

  constructor(
    private clubsService: AdminClubsService,
    private racesService: AdminRacesService,
    private eventsService: AdminEventsService,
    private entriesService: AdminEntriesService,
    private selectedRaceService: SelectedRaceService,
    private router: Router
   ) {}

  public ngOnInit() {
    this.selectedRace = this.selectedRaceService.getSelectedRace.value;
    this.entriesSubscription = this.entriesService.getEntries()
      .subscribe((entries: Entry[]) => {
        this.entries = entries;
        if (this.selectedRace) {
          this.entriesForRace = this.getEntriesForRace(this.selectedRace);
        }
    });

    this.clubsSubscription = this.clubsService.getClubs().subscribe((clubs: Club[]) => {
        this.clubs = clubs;
        if (this.selectedRace) {
          this.entriesForRace = this.getEntriesForRace(this.selectedRace);
        }
    });

    this.eventsSubscription = this.eventsService.getEvents().subscribe((events: Event[]) => {
        this.events = events;
        if (this.selectedRace) {
          this.entriesForRace = this.getEntriesForRace(this.selectedRace);
        }
    });

    this.racesSubscription = this.racesService.getRaces().subscribe((races: Race[]) => {
        this.races = races;
        if (races.length > 0) {
          if (!this.selectedRace) {
            this.selectedRace = this.races[0];
          }
          this.changeRace(this.selectedRace);
        }
    });

    this.selectedRaceSubscription =
      this.selectedRaceService.getSelectedRace.subscribe((race: Race) => {
        if (race !== null) {
          this.selectedRace = race;
          this.entriesForRace = this.getEntriesForRace(this.selectedRace);
        }
    });
  }

  public ngOnDestroy() {
    this.clubsSubscription.unsubscribe();
    this.eventsSubscription.unsubscribe();
    this.entriesSubscription.unsubscribe();
    this.racesSubscription.unsubscribe();
    this.selectedRaceSubscription.unsubscribe();
  }

  public removeEntry(entry: Entry): void {
    console.log('Remove ' + entry.id);
    const indexOfEntry: number = this.entriesForRace.indexOf(entry);
    const updatedEntries: Entry[] = new Array<Entry>();
    let lastNumber: number = 1;
    if (indexOfEntry > 1) {
      for (let i = indexOfEntry; i >= 0; i--) {
        const prevEntry: Entry = this.entriesForRace[i];
        if (!prevEntry.fixedNumber) {
          lastNumber = prevEntry.number;
          break;
        }
      }
    }
    for (let i = indexOfEntry + 1; i < this.entriesForRace.length; i++) {
      const nextEntry: Entry = this.entriesForRace[i];
      nextEntry.raceOrder = nextEntry.raceOrder - 1;
      if (!nextEntry.fixedNumber) {
        nextEntry.number = lastNumber;
        lastNumber = lastNumber + 1;
      }
      updatedEntries.push(nextEntry);
    }
    this.entriesService.removeEntry(entry).then((e: Entry) => {
      if (updatedEntries.length > 0) {
        this.entriesService.updateEntries(updatedEntries);
      }
    });
  }

  public changeRace(race: Race): void {
    this.selectedRace = race;
    this.selectedRaceService.setSelectedRace = race;
    this.entriesForRace = this.getEntriesForRace(race);
  }

  public getEventNameForEntry(entry: Entry): string {
      let eventName: string;
      const event: Event = this.getEventForId(entry.event);
      if (event) {
          eventName = event.name;
      }
      return eventName;
  }

  public getClubNamesForEntry(entry: Entry): string {
      let clubs: string;
      for (const clubId of entry.clubs) {
          const club: Club = this.getClubForId(clubId);
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

  private getEntriesForRace(race: Race): Entry[] {
    const entries: Entry[] = this.entries
      .filter((entry: Entry) => entry.race === race.id);
    entries.sort((a: Entry, b: Entry) => {
        if (a.number < b.number) {
            return -1;
        } else if (a.number === b.number) {
            return 0;
        } else if (a.number > b.number) {
            return 1;
        }
    });
    return entries;
  }

  private getEventForId(eventId: number): Event {
      for (const event of this.events) {
          if (event.id === eventId) {
              return event;
          }
      }
      return null;
  }

  private getClubForId(clubId: number): Club {
      for (const club of this.clubs) {
          if (club.id === clubId) {
              return club;
          }
      }
      return null;
  }
}
