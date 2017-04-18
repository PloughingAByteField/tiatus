import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { Penalty } from './penalty.model';

import { PenaltiesHttpService } from './penalties-http.service';

@Injectable()
export class PenaltiesService {
    protected penalties: Penalty[] = new Array<Penalty>();
    protected penaltiesSubject: BehaviorSubject<Penalty[]>
        = new BehaviorSubject<Penalty[]>(this.penalties);

    protected requested: boolean = false;

    constructor(protected service: PenaltiesHttpService) {}

    public getPenalties(): BehaviorSubject<Penalty[]> {
        if (!this.requested) {
            this.requested = true;
            this.refresh();
        }
        return this.penaltiesSubject;
    }

    public refresh(): void {
        this.service.getPenalties().subscribe((penalties: Penalty[]) => {
            this.penalties = penalties;
            this.penaltiesSubject.next(this.penalties);
        });
    }
}
