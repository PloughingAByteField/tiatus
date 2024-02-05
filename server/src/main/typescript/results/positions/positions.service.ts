import { Injectable } from '@angular/core';

import { PositionsService } from '../../positions/positions.service';
import { ResultsHttpPositionsService } from './positions-http.service';

import { Position } from '../../positions/position.model';

@Injectable()
export class ResultsPositionsService extends PositionsService {

    constructor(protected service: ResultsHttpPositionsService) {
        super(service);
    }

    public refresh(): void {
        this.service.getPositions().subscribe((positions: Position[]) => {
            if (positions != null) {
                this.positions = positions;
                this.subject.next(this.positions);
            }
        });
    }
}
