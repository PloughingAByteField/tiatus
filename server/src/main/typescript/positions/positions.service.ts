import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { Position, convertObjectoPosition } from './position.model';

import { PositionsHttpService } from './positions-http.service';

import { Message } from '../websocket/message.model';
import { MessageType } from '../websocket/message-type.model';

@Injectable()
export class PositionsService {
    protected positions: Position[] = new Array<Position>();
    protected subject: BehaviorSubject<Position[]>
        = new BehaviorSubject<Position[]>(this.positions);

    protected requested: boolean = false;

    constructor(protected service: PositionsHttpService) {}

    public getPositions(): BehaviorSubject<Position[]> {
        if (!this.requested) {
            this.requested = true;
            this.refresh();
        }
        return this.subject;
    }

    public refresh(): void {
        this.service.getPositions().subscribe((positions: Position[]) => {
            this.positions = positions;
            this.subject.next(this.positions);
        });
    }

    public processMessage(message: Message): void {
        console.log('process message');
        console.log(message);
        let position: Position = convertObjectoPosition(message.data);
        console.log(position);
        if (message.type === MessageType.ADD) {
            this.positions.push(position);

        } else if (message.type === MessageType.DELETE) {
            let deletedPosition: Position = this.getPositionForId(position.id);
            if (deletedPosition !== null) {
                let index = this.positions.indexOf(deletedPosition);
                let sliced = this.positions.splice(index, 1);
            }

        } else if (message.type === MessageType.UPDATE) {
            let updatedPosition: Position = this.getPositionForId(position.id);
            if (updatedPosition !== null) {
                updatedPosition.name = position.name;
            }
        }

        this.subject.next(this.positions);
    }

    public getPositionForId(positionId: number): Position {
        for (let position of this.positions) {
            if (position.id === positionId) {
                return position;
            }
        }
        return null;
    }

    public getPositionForName(name: string): Position {
        for (let position of this.positions) {
            if (position.name === name) {
                return position;
            }
        }
        return null;
    }
}
