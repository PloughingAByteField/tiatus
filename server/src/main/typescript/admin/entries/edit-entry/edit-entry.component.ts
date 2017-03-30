import { Component, OnInit, OnDestroy } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute, Params } from '@angular/router';
import { FormBuilder, FormControl, FormGroup, FormArray } from '@angular/forms';
import { Validators, AbstractControl, ValidatorFn } from '@angular/forms';

import { Subscription } from 'rxjs/Subscription';

import { Race } from '../../../races/race.model';
import { Entry } from '../../../entries/entry.model';
import { Event } from '../../../events/event.model';
import { Club } from '../../../clubs/club.model';
import { RaceEvent } from '../../../race-events/race-event.model';

import { SelectedRaceService } from '../../races/selected-race.service';
import { AdminRacesService } from '../../races/races.service';
import { AdminEventsService } from '../../events/events.service';
import { AdminClubsService } from '../../clubs/clubs.service';
import { AdminEntriesService } from '../entries.service';
import { AdminUnassignedEventsService } from '../../events/unassigned-events.service';
import { AdminRaceEventsService } from '../../events/race-events.service';

@Component({
  selector: 'edit-entry',
  styleUrls: [ './edit-entry.component.css' ],
  templateUrl: './edit-entry.component.html'
})
export class EditEntryComponent implements OnInit, OnDestroy {

  public editEntryForm: FormGroup;
  public selectedRace: Race;
  public selectedEvent: Event;
  public entry: Entry;
  public eventsForRace: Event[] = new Array<Event>();
  public clubs: Club[] = new Array<Club>();

  private racesSubscription: Subscription;
  private entriesSubscription: Subscription;
  private eventsSubscription: Subscription;
  private raceEventsSubscription: Subscription;
  private unassignedEventsSubscription: Subscription;
  private clubsSubscription: Subscription;
  private races: Race[];
  private entries: Entry[] = new Array<Entry>();
  private events: Event[] = new Array<Event>();
  private raceEvents: RaceEvent[] = new Array<RaceEvent>();
  private unassignedEvents: Event[] = new Array<Event>();
  private entryId: number;

  constructor(
    private racesService: AdminRacesService,
    private selectedRaceService: SelectedRaceService,
    private eventsService: AdminEventsService,
    private entriesService: AdminEntriesService,
    private raceEventsService: AdminRaceEventsService,
    private unassignedEventsService: AdminUnassignedEventsService,
    private clubsService: AdminClubsService,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private location: Location
  ) {}

  public ngOnInit() {
    this.editEntryForm = this.fb.group({
      crew: this.fb.control('', [
        Validators.minLength(1),
        Validators.maxLength(250),
      ]),
      isFixedNumber: this.fb.control(false),
      eventsType: this.fb.control('current'),
      clubs: this.fb.control('', Validators.required),
      event: this.fb.control(''),
      race: this.fb.control(''),
      fixedNumber: this.fb.control('', this.validateEntryNumber()),
      weighting: this.fb.control(''),
    });

    this.route.params.subscribe((params: Params) => {
        this.entryId = +params['entryId'];
        console.log('this.entryId ' + this.entryId);
        this.setEntry(this.entryId);
    });

    this.selectedRace = this.selectedRaceService.getSelectedRace.value;
    this.racesSubscription = this.racesService.getRaces()
      .subscribe((races: Race[]) => {
        this.races = races;
        if (this.entryId) {
          this.setEntry(this.entryId);
        }
    });

    this.entriesSubscription = this.entriesService.getEntries()
      .subscribe((entries: Entry[]) => {
        this.entries = entries;
        if (this.entryId) {
          this.setEntry(this.entryId);
        }
    });

    this.clubsSubscription = this.clubsService.getClubs()
      .subscribe((clubs: Club[]) => {
        this.clubs = clubs;
    });

    this.eventsSubscription = this.eventsService.getEvents().subscribe((events: Event[]) => {
        this.events = events;
        if (events.length > 0) {
          if (this.selectedRace) {
            this.eventsForRaceChange(this.selectedRace);
          }
          if (this.entryId) {
            this.setEntry(this.entryId);
          }
        }
    });

    this.raceEventsSubscription = this.raceEventsService.getEvents()
      .subscribe((events: RaceEvent[]) => {
        if (events.length > 0) {
          this.raceEvents = events;
          if (this.entryId) {
            this.setEntry(this.entryId);
          }
          if (this.selectedRace) {
            this.eventsForRaceChange(this.selectedRace);
          }
        }
    });

    this.unassignedEventsSubscription = this.unassignedEventsService.getEvents()
          .subscribe((unassigned: Event[]) => this.unassignedEvents = unassigned);
  }

  public ngOnDestroy() {
    this.racesSubscription.unsubscribe();
    this.entriesSubscription.unsubscribe();
    this.eventsSubscription.unsubscribe();
    this.raceEventsSubscription.unsubscribe();
    this.unassignedEventsSubscription.unsubscribe();
    this.clubsSubscription.unsubscribe();
  }

  public changeRace(race: Race): void {
    console.log(race);
    this.eventsForRaceChange(race);
  }

  public goBack(): void {
    this.location.back();
  }

  public onEventTypeChange(type: string): void {
    console.log('change');
    console.log(type);
    switch (type) {
      case 'current':
        console.log('got current');
        this.setEventsToCurrentRace();
        break;
      case 'unassigned':
        console.log('got unassigned');
        this.setEventsToUnassigned();
        break;
      case 'other':
        console.log('got other');
        this.setEventsToOtherRaces();
        break;
      default:
        console.log('got unknown');
        break;
    }
  }

  public onSubmit({ value, valid }: { value: any, valid: boolean }) {
    console.log('on submit');
    console.log(value);
    let entry: Entry = new Entry();
    entry.id = this.entry.id;
    entry.clubs = new Array<number>();
    for (let club of value.clubs) {
      entry.clubs.push(club.id);
    }
    entry.event = value.event.id;
    entry.race = value.race.id;
    entry.crew = value.crew;
    entry.fixedNumber = value.fixedNumber;
    if (value.fixedNumber) {
      entry.number = value.fixedNumber;
    }
    this.entriesService.updateEntry(entry).then((updatedEntry: Entry) => {
      this.entry.clubs = entry.clubs;
      this.entry.event = entry.event;
      this.entry.race = entry.race;
      this.entry.crew = entry.crew;
      this.entry.fixedNumber = entry.fixedNumber;
      if (entry.fixedNumber) {
        this.entry.number = entry.number;
      }
      this.goBack();
    });
  }

  public getRaceNameForEvent(event: Event): string {
    if (!this.editEntryForm.get('eventsType')) {
      return null;
    }
    if (this.editEntryForm.get('eventsType').value === 'unassigned') {
      return 'unassigned';
    }
    for (let raceEvent of this.raceEvents) {
      if (raceEvent.event === event.id) {
        return this.racesService.getRaceForId(raceEvent.race).name;
      }
    }
    return null;
  }

  private isEventInUnassigned(): boolean {
    for (let e of this.unassignedEvents) {
      if (e.id === this.selectedEvent.id) {
        return true;
      }
    }
    return false;
  }

  private isEventIsInRace(): boolean {
    let raceForEntry: Race = this.racesService.getRaceForId(this.entry.race);
    for (let raceEvent of this.raceEvents) {
      if (raceEvent.event === this.selectedEvent.id) {
        console.log(raceEvent);
        if (raceForEntry.id === raceEvent.race) {
          console.log('match');
          return true;
        }
        break;
      }
    }
    return false;
  }

  private setEventsToCurrentRace(): void {
    this.eventsForRace = this.getEventsForRace(this.selectedRace);
    if (this.eventsForRace.length > 0) {
      if (this.isEventIsInRace()) {
        this.editEntryForm.get('event').setValue(this.selectedEvent);
      } else {
        this.editEntryForm.get('event').setValue(this.eventsForRace[0]);
      }
    }
  }

  private setEventsToOtherRaces(): void {
    this.eventsForRace = this.getEventsNotInRace(this.selectedRace);
    let selectedEventInRace: boolean = false;
    if (this.eventsForRace.length > 0) {
     if (!this.isEventIsInRace()) {
        this.editEntryForm.get('event').setValue(this.selectedEvent);
      } else {
        this.editEntryForm.get('event').setValue(this.eventsForRace[0]);
      }
    }
  }

  private setEventsToUnassigned(): void {
    this.eventsForRace = this.unassignedEvents;
    let selectedEventInUassigned: boolean = false;
    if (this.eventsForRace.length > 0) {
      if (this.isEventInUnassigned()) {
        this.editEntryForm.get('event').setValue(this.selectedEvent);
      } else {
        this.editEntryForm.get('event').setValue(this.eventsForRace[0]);
      }
    }
  }

  private getEventsForRace(race: Race): Event[] {
    let raceEventsForRace: RaceEvent[] = this.raceEvents
      .filter((raceEvent: RaceEvent) => raceEvent.race === race.id);
    let eventsForRace: Event[] = new Array<Event>();
    raceEventsForRace.map((raceEvent: RaceEvent) => {
      for (let event of this.events) {
        if (event.id === raceEvent.event) {
          eventsForRace.push(event);
          break;
        }
      }
    });
    return eventsForRace;
  }

  private getEventsNotInRace(race: Race): Event[] {
    let raceEventsForRace: RaceEvent[] = this.raceEvents
      .filter((raceEvent: RaceEvent) => raceEvent.race !== race.id);
    let eventsForRace: Event[] = new Array<Event>();
    raceEventsForRace.map((raceEvent: RaceEvent) => {
      for (let event of this.events) {
        if (event.id === raceEvent.event) {
          eventsForRace.push(event);
          break;
        }
      }
    });
    return eventsForRace;
  }

  private eventsForRaceChange(race: Race): void {
    this.onEventTypeChange(this.editEntryForm.get('eventsType').value);
  }

  private setEntry(entryId: number): void {
    if (this.entries.length > 0 && this.events.length > 0
      && this.races.length > 0 && this.raceEvents.length > 0) {
      for (let entry of this.entries) {
        if (entry.id ===  entryId) {
          this.entry = entry;
          this.selectedEvent = this.eventsService.getEventForId(this.entry.event);
          this.setEntryDetailsInForm(entry);
          return;
        }
      }
    }
  }

  private setEntryDetailsInForm(entry: Entry): void {
    if (this.editEntryForm) {
      let race: Race = this.racesService.getRaceForId(entry.race);
      this.selectedRace = race;
      let event: Event = this.eventsService.getEventForId(entry.event);
      this.editEntryForm.get('race').setValue(race);
      this.editEntryForm.get('event').setValue(event);
      this.editEntryForm.get('crew').setValue(entry.crew);
      let clubs: Club[] = new Array<Club>();
      for (let clubId of entry.clubs) {
        let club = this.clubsService.getClubForId(clubId);
        if (club) {
          clubs.push(club);
        }
      }
      this.editEntryForm.get('clubs').setValue(clubs);
      this.editEntryForm.get('event').setValue(this.selectedEvent);
      if (this.isEventIsInRace()) {
        console.log('current');
        this.editEntryForm.get('eventsType').setValue('current');
      } else {
        if (this.isEventInUnassigned()) {
          this.editEntryForm.get('eventsType').setValue('unassigned');
        } else {
          console.log('other');
          this.editEntryForm.get('eventsType').setValue('other');
        }
      }
      console.log(entry);
      console.log(event);
      console.log(this.selectedEvent);
      console.log(this.editEntryForm);
    }
  }

  private validateEntryNumber(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.editEntryForm && this.editEntryForm.get('isFixedNumber')
        && this.editEntryForm.get('isFixedNumber').value === true) {
        console.log(control);
        if (this.isEmptyInputValue(control.value)) {
          return { tooSmall: true };
        }

        if (control.value <= 0) {
          return { tooSmall: true };
        }

        if (control.value >= 100000) {
          return { tooLarge: true };
        }

        // for (let entry of this.entriesForRace) {
        //   if (entry.number === control.value) {
        //     console.log(entry);
        //     return { existingNumber: true };
        //   }
        // }
      }

      return null;
    };
  }

  private isEmptyInputValue(value: any): boolean {
    return value == null || value.length === 0;
  }
}
