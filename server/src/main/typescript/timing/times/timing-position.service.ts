import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Subject } from 'rxjs';
import { Position } from '../../positions/position.model';

@Injectable()
export class TimingPositionService {
    private position: Subject<Position> = new BehaviorSubject<Position>(null);

    constructor() {
        this.init();
    }

    private init() {
        this.position = new BehaviorSubject<Position>(JSON.parse(localStorage.getItem('position')));
    }

    public get getPosition(): Subject<Position> {
        return this.position;
    }

    public set setPosition(p: Position) {
        localStorage.setItem('position', JSON.stringify(p));
        this.position.next(p);
    }
}
