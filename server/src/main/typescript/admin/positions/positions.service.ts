import { Injectable } from '@angular/core';

import { Position } from '../../positions/position.model';
import { PositionsService } from '../../positions/positions.service';
import { AdminPositionsHttpService } from './positions-http.service';

@Injectable()
export class AdminPositionsService extends PositionsService {

    constructor(protected service: AdminPositionsHttpService) {
        super(service);
    }

    public addPosition(position: Position): void {
        this.service.createPosition(position).then((p: Position) => {
            this.positions.push(p);
            this.subject.next(this.positions);
        });
    }

    public removePosition(position: Position): void {
        this.service.removePosition(position).then((p: Position) => {
            const index = this.positions.indexOf(p);
            const sliced = this.positions.splice(index, 1);
            this.subject.next(this.positions);
        });
    }

    public updatePosition(position: Position): void {
        this.service.updatePosition(position).then();
    }
}
