import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { Position } from './position.model';

import { PositionsHttpService } from './positions-http.service';

@Injectable()
export class PositionsService {
    protected positions: Position[] = new Array<Position>();
    protected subject: BehaviorSubject<Position[]>
        = new BehaviorSubject<Position[]>(this.positions);

    constructor(protected service: PositionsHttpService) {
        this.refresh();
    }

    public getPositions(): BehaviorSubject<Position[]> {
        return this.subject;
    }

    public refresh(): void {
        this.service.getPositions().subscribe((positions: Position[]) => {
            this.positions = positions;
            this.subject.next(this.positions);
        });
    }
}
