import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

import { Race, convertObjectToRace } from './race.model';

import { Message } from '../websocket/message.model';
import { MessageType } from '../websocket/message-type.model';

import { RacesHttpService } from './races-http.service';

@Injectable()
export class RacesService {
    protected races: Race[] = new Array<Race>();
    protected requested: boolean = false;

    protected racesSubject: BehaviorSubject<Race[]>
        = new BehaviorSubject<Race[]>(this.races);

    constructor(protected service: RacesHttpService) {}

    public getRaceForId(id: number): Race {
      return this.races.filter((race: Race) => race.id === id).shift();
    }

    public getRaces(): BehaviorSubject<Race[]> {
        if (!this.requested) {
            this.requested = true;
            this.refresh();
        }
        return this.racesSubject;
    }

    public refresh(): void {
         this.service.getRaces().subscribe((races: Race[]) => {
            if (races != null) {
                this.races = races;
                this.racesSubject.next(this.races);
            }
        });
    }

    public processMessage(message: Message): void {
        console.log('process message');
        console.log(message);
        const race: Race = convertObjectToRace(message.data);
        console.log(race);
        if (message.type === MessageType.ADD) {
            this.races.push(race);

        } else if (message.type === MessageType.DELETE) {
            const deletedRace: Race = this.getRaceForId(race.id);
            if (deletedRace !== null) {
                const index = this.races.indexOf(deletedRace);
                const sliced = this.races.splice(index, 1);
            }

        } else if (message.type === MessageType.UPDATE) {
            const updatedRace: Race = this.getRaceForId(race.id);
            if (updatedRace !== null) {
                updatedRace.active = race.active;
                updatedRace.name = race.name;
                updatedRace.closed = race.closed;
                updatedRace.raceOrder = race.raceOrder;
                updatedRace.drawLocked = race.drawLocked;
            }
        }

        this.racesSubject.next(this.races);
    }

}
