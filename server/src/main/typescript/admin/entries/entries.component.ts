import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';

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

  private entries: Entry[];
  private clubs: Club[] = new Array<Club>();
  private events: Event[] = new Array<Event>();
  private raceId: number;

  private entriesSubscription: Subscription;
  private eventsSubscription: Subscription;
  private clubsSubscription: Subscription;
  private racesSubscription: Subscription;
  private selectedRaceSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private clubsService: AdminClubsService,
    private racesService: AdminRacesService,
    private eventsService: AdminEventsService,
    private entriesService: AdminEntriesService,
    private selectedRaceService: SelectedRaceService,
    private router: Router
   ) {}

  public ngOnInit() {
    this.entriesSubscription = this.entriesService.getEntries()
      .subscribe((entries: Entry[]) => {
        this.entries = entries;
        if (this.selectedRace) {
          this.changeRace(this.selectedRace);
        }
    });

    this.clubsSubscription = this.clubsService.getClubs().subscribe((clubs: Club[]) => {
        this.clubs = clubs;
        if (this.selectedRace) {
          this.changeRace(this.selectedRace);
        }
    });

    this.eventsSubscription = this.eventsService.getEvents().subscribe((events: Event[]) => {
        this.events = events;
        if (this.selectedRace) {
          this.changeRace(this.selectedRace);
        }
    });

    this.racesSubscription = this.racesService.getRaces().subscribe((races: Race[]) => {
        this.races = races;
        if (races.length > 0) {
          let race: Race;
          if (this.raceId) {
            race = this.racesService.getRaceForId(this.raceId);
          } else {
            race = this.races[0];
          }
          this.changeRace(race);
        }
    });

    this.route.params.subscribe((params: Params) => {
        this.raceId = +params['raceId'];
        let race: Race = this.racesService.getRaceForId(this.raceId);
        if (race) {
          this.changeRace(race);
        }
    });

    this.selectedRaceSubscription =
      this.selectedRaceService.getSelectedRace.subscribe((race: Race) => {
        if (race !== null) {
          this.changedRace(race);
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
  }

  public changeRace(race: Race): void {
    this.selectedRace = race;
    this.selectedRaceService.setSelectedRace = race;
    this.entriesForRace = this.entries.filter((entry: Entry) => entry.race === race.id);
  }

  public changedRace(race: Race): void {
    // this.router.navigate(['/entries', race.id]);
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
