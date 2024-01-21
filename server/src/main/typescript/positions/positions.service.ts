import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';

import { Position, convertObjectoPosition } from './position.model';
import { Data } from '../model/data.model';

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
        this.service.getPositions().subscribe((data: Data) => {
            this.positions = data.data;
            this.subject.next(this.positions);
        });
    }

    public processMessage(message: Message): void {
        console.log('process message');
        console.log(message);
        const position: Position = convertObjectoPosition(message.data);
        console.log(position);
        if (message.type === MessageType.ADD) {
            this.positions.push(position);

        } else if (message.type === MessageType.DELETE) {
            const deletedPosition: Position = this.getPositionForId(position.id);
            if (deletedPosition !== null) {
                const index = this.positions.indexOf(deletedPosition);
                const sliced = this.positions.splice(index, 1);
            }

        } else if (message.type === MessageType.UPDATE) {
            const updatedPosition: Position = this.getPositionForId(position.id);
            if (updatedPosition !== null) {
                updatedPosition.name = position.name;
            }
        }

        this.subject.next(this.positions);
    }

    public getPositionForId(positionId: number): Position {
        for (const position of this.positions) {
            if (position.id === positionId) {
                return position;
            }
        }
        return null;
    }

    public getPositionForName(name: string): Position {
        for (const position of this.positions) {
            if (position.name === name) {
                return position;
            }
        }
        return null;
    }
}
