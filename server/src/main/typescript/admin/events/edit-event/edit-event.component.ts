import { Component, OnInit, OnDestroy } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute, Params } from '@angular/router';
import { FormBuilder, FormControl, FormGroup, FormArray } from '@angular/forms';
import { Validators, AbstractControl, ValidatorFn } from '@angular/forms';

import { Subscription } from 'rxjs';

import { Race } from '../../../races/race.model';

import { Event } from '../../../events/event.model';
import { EventPosition } from '../../../events/event-positions.model';
import { AdminEventsService } from '../events.service';

import { Position } from '../../../positions/position.model';
import { AdminPositionsService } from '../../positions/positions.service';

import { RaceEvent } from '../../../race-events/race-event.model';
import { AdminUnassignedEventsService } from '../unassigned-events.service';

import { AdminRaceEventsService } from '../race-events.service';
import { AdminRacesService } from '../../races/races.service';

@Component({
  selector: 'edit-event',
  styleUrls: [ './edit-event.component.css' ],
  templateUrl: './edit-event.component.html'
})
export class EditEventComponent implements OnInit, OnDestroy {

  public editEventForm: FormGroup;

  public selectedEvent: Event;
  public positions: Position[] = new Array<Position>();
  public unassignedPositions: Position[] = new Array<Position>();

  private eventId: number;
  private eventInRace: Race;
  private events: Event[] = new Array<Event>();
  private eventsInRace: Event[] = new Array<Event>();
  private races: Race[] = new Array<Race>();
  private raceEvents: RaceEvent[] = new Array<RaceEvent>();
  private unassignedEvents: Event[] = new Array<Event>();

  private positionsSubscription: Subscription;
  private eventsSubscription: Subscription;
  private unassignedEventsSubscription: Subscription;
  private raceEventsSubscription: Subscription;
  private racesSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private positionsService: AdminPositionsService,
    private racesService: AdminRacesService,
    private eventsService: AdminEventsService,
    private unassignedEventsService: AdminUnassignedEventsService,
    private raceEventsService: AdminRaceEventsService,
    private fb: FormBuilder,
    private location: Location
  ) {}

  public ngOnInit() {
    this.editEventForm = this.fb.group({
      name: this.fb.control('', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(250),
        this.existingEventName()
      ]),
      isNonRaceAssigned: this.fb.control(false),
      newPosition: this.fb.control(''),
      positions: this.fb.array([])
    });

    this.route.params.subscribe((params: Params) => {
        this.eventId = +params['eventId'];
        console.log('this.eventId ' + this.eventId);
        this.setSelectedEvent(this.getEventForId(this.eventId));
    });

    this.positionsSubscription = this.positionsService.getPositions()
      .subscribe((positions: Position[]) => {
        this.positions = positions;
    });

    this.eventsSubscription = this.eventsService.getEvents()
      .subscribe((events: Event[]) => {
        this.events = events;
        if (events.length > 0 && this.eventId) {
          this.setSelectedEvent(this.getEventForId(this.eventId));
        }
    });

    this.racesSubscription = this.racesService.getRaces()
      .subscribe((races: Race[]) => {
        this.races = races;
        if (races.length > 0) {
          this.populateEventsForRace(this.selectedEvent);
        }
    });

    this.raceEventsSubscription = this.raceEventsService.getEvents()
      .subscribe((events: RaceEvent[]) => {
        this.raceEvents = events;
        if (events.length > 0) {
          this.populateEventsForRace(this.selectedEvent);
        }
    });

    this.unassignedEventsSubscription = this.unassignedEventsService.getEvents()
      .subscribe((events: Event[]) => this.unassignedEvents = events);
  }

  public ngOnDestroy() {
    this.positionsSubscription.unsubscribe();
    this.eventsSubscription.unsubscribe();
    this.unassignedEventsSubscription.unsubscribe();
    this.raceEventsSubscription.unsubscribe();
    this.racesSubscription.unsubscribe();
  }

  public onSubmitN(value: any): void {
    console.log(value);
  }

  public onSubmit({ value, valid }: { value: any, valid: boolean }) {
    console.log('on submit');
    console.log(this.selectedEvent);
    const updatedEvent: Event = new Event();
    updatedEvent.id = this.selectedEvent.id;
    updatedEvent.name = this.editEventForm.get('name').value;
    updatedEvent.positions = new Array<EventPosition>();
    const array: FormArray = this.editEventForm.get('positions') as FormArray;
    for (const group of array.controls) {
      console.log(group);
      const eventPosition: EventPosition = new EventPosition();
      eventPosition.event = this.selectedEvent.id;
      eventPosition.position = +group.get('position').value;
      eventPosition.positionOrder = +group.get('positionOrder').value;
      updatedEvent.positions.push(eventPosition);
    }
    console.log(updatedEvent);
    this.eventsService.updateEvent(updatedEvent).then((event: Event) => this.goBack());
  }

  public removePosition(index: number): void {
    const array: FormArray = this.editEventForm.get('positions') as FormArray;
    for (let i = index + 1; i < array.controls.length; i++) {
      const controlGroup: FormGroup = array.controls[i] as FormGroup;
      const orderValue: number = controlGroup.get('positionOrder').value;
      const orderControl: AbstractControl = controlGroup.get('positionOrder');
      orderControl.setValue(orderValue - 1);
    }
    const group: FormGroup = array.controls[index] as FormGroup;
    array.removeAt(index);
    group.removeControl('position');
    group.removeControl('positionOrder');
    group.removeControl('eventPosition');
    this.fillUnassignedPositions();
    array.markAsDirty();
  }

  public addPosition(): void {
    const newPosition: Position = this.editEventForm.get('newPosition').value;
    const array: FormArray = this.editEventForm.get('positions') as FormArray;
    const newEventPosition: EventPosition = new EventPosition();
    newEventPosition.event = this.selectedEvent.id;
    newEventPosition.position = newPosition.id;
    newEventPosition.positionOrder = 1;
    if (array.controls.length > 0) {
      newEventPosition.positionOrder =
        array.controls[array.controls.length - 1].get('positionOrder').value + 1;
    }
    this.addPositionToForm(newEventPosition, array, true);

    // remove from unassigned
    this.unassignedPositions.splice(this.unassignedPositions.indexOf(newPosition), 1);
    this.fillUnassignedPositions();
  }

  public goBack(): void {
    this.location.back();
  }

  public getPositionNameForPositionId(positionId: number) {
    for (const position of this.positions) {
      if (position.id === positionId) {
        return position.name;
      }
    }
    return null;
  }

  public canEnableSubmitButton(): boolean {
    if (!this.editEventForm.dirty) {
      return false;
    }
    if (this.editEventForm.valid) {
      console.log(this.editEventForm.get('name').dirty);
      if (this.editEventForm.get('name').dirty) {
        if (this.editEventForm.get('name').value !== this.selectedEvent.name) {
          console.log('ss');
          return true;
        }
        console.log('aa');
      }

      console.log(this.editEventForm.get('positions').dirty);
      const array: FormArray = this.editEventForm.get('positions') as FormArray;
      if (this.editEventForm.get('positions').dirty) {
        if (array.controls.length !== this.selectedEvent.positions.length) {
          console.log('have position addition/removal');
          if (array.controls.length > 1) {
            return true;
          } else {
            return false;
          }
        }
        for (const group of array.controls) {
          if (group.dirty) {
            const eventPosition: EventPosition = group.get('eventPosition').value;
            console.log(eventPosition);
            const position: number = group.get('position').value;
            console.log(position);
            if (eventPosition.position !== +position) {
              console.log('changed position');
              return true;
            }
            const index: number = array.controls.indexOf(group);
            console.log('index is ' + index);
            if (this.selectedEvent.positions[index].position !== +position) {
              console.log('mismatched positions');
              return true;
            }
          }
        }
      } else {
        console.log('not touched');
      }

      if (array.controls.length < 2) {
        return false;
      }
    }

    return false;
  }

  private getRaceForId(raceId: number): Race {
    for (const race of this.races) {
      if (race.id === raceId) {
        return race;
      }
    }
    return null;
  }

  private getRaceContainingEvent(event: Event): Race {
    for (const raceEvent of this.raceEvents) {
      if (raceEvent.event === event.id) {
        return this.getRaceForId(raceEvent.race);
      }
    }
    return null;
  }

  private populateEventsForRace(event: Event): void {
    if (event !== null) {
      this.eventInRace = this.getRaceContainingEvent(event);
      if (this.eventInRace !== null) {
        const raceEvents: RaceEvent[] = this.raceEvents
          .filter((raceEvent: RaceEvent) => raceEvent.race === this.eventInRace.id);
        this.eventsInRace = new Array<Event>();
        raceEvents.map((raceEvent: RaceEvent) =>
          this.eventsInRace.push(this.getEventForId(raceEvent.event)));
      }
    }
  }

  private addPositionToForm(
    eventPosition: EventPosition, positionsFormArray: FormArray, markDirty: boolean): void {
    const eventPositionControl: FormControl = this.fb.control(eventPosition);
    const positionControl: FormControl = this.fb.control(eventPosition.position);
    const positionOrderControl: FormControl = this.fb.control(eventPosition.positionOrder);
    const group: FormGroup = this.fb.group({
      eventPosition: eventPositionControl,
      position: positionControl,
      positionOrder: positionOrderControl
    }, {
      validator: this.checkExistingPosition()
    });
    positionsFormArray.push(group);

    if (markDirty) {
      // have form revalidate
      group.markAsDirty();
    }
  }

  private setSelectedEvent(event: Event): void {
    this.selectedEvent = event;
    if (this.selectedEvent !== null) {
      this.populateEventsForRace(this.selectedEvent);
      this.editEventForm.get('name').setValue(event.name);
      const positionsFormArray: FormArray = this.fb.array([]);
      for (const eventPosition of this.selectedEvent.positions) {
        this.addPositionToForm(eventPosition, positionsFormArray, false);
      }
      this.editEventForm.setControl('positions', positionsFormArray);
      this.fillUnassignedPositions();
    }
  }

  private getEventForId(eventId: number): Event {
    if (this.events) {
      for (const event of this.events) {
        if (event.id === eventId) {
          return event;
        }
      }
    }
    return null;
  }

  private fillUnassignedPositions(): void {
    this.unassignedPositions = this.positions.filter((position: Position) =>
          !this.positionInForm(position));
    if (this.unassignedPositions.length > 0) {
      this.editEventForm.setControl('newPosition', this.fb.control(this.unassignedPositions[0]));
    }
  }

  private positionInForm(position: Position): boolean {
    const array: FormArray = this.editEventForm.get('positions') as FormArray;
    for (const control of array.controls) {
      const p: EventPosition = control.get('eventPosition').value;
      if (position.id === p.position) {
        return true;
      }
    }
    return false;
  }

  private checkExistingPosition(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (control.dirty) {
        console.log(control.value);
        if (control.get('position')) {
          const position: number = +control.get('position').value as number;
          const array: FormArray =  this.editEventForm.get('positions') as FormArray;
          for (const group of array.controls) {
            if (control !== group) {
              const groupPositon: number = +group.get('position').value as number;
              if (position === groupPositon) {
                console.log('have same position');
                return { existingPosition: true };
              }
            }
          }
        }
      }
      return null;
    };
  }

  private existingEventName(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.selectedEvent) {
        if (this.eventInRace) {
          for (const event of this.eventsInRace) {
            if (event.name === control.value && event.name !== this.selectedEvent.name) {
              console.log('match for ' + event.name);
              return { existingName: true };
            }
          }
        } else {
          for (const event of this.unassignedEvents) {
            if (event.name === control.value && event.name !== this.selectedEvent.name) {
              console.log('match for ' + event.name);
              return { existingName: true };
            }
          }
        }
      }

      return null;
    };
  }
}
