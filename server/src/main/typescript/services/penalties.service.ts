import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { Penalty } from '../models/penalty.model';

import { PenaltiesHttpService } from '../http-services/penalties.service';

@Injectable()
export class PenaltiesService {
    private penalties: BehaviorSubject<Penalty[]>;

    constructor(private service: PenaltiesHttpService) {
        this.penalties = new BehaviorSubject<Penalty[]>(new Array<Penalty>());
        this.service.getPenalties().subscribe((penalties: Penalty[]) => {
            console.log(penalties);
            this.penalties.next(penalties);
        });
    }

    public getPenalties(): BehaviorSubject<Penalty[]> {
        return this.penalties;
    }
}
