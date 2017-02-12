import { EventEmitter, Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subject } from 'rxjs/Subject';
import { Position } from '../../models/position.model';

@Injectable()
export class TimingPositionService {
    private position: Subject<Position> = new BehaviorSubject<Position>(null);

    public get getPosition(): Subject<Position> {
        return this.position;
    }

    public set setPosition(p: Position) {
        this.position.next(p);
    }
}
