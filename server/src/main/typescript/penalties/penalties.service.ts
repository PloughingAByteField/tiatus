import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';

import { Penalty } from './penalty.model';
import { Data } from '../model/data.model';

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
        this.service.getPenalties().subscribe((data: Data) => {
            if (data.data !== undefined) {
                this.penalties = data.data;
                this.penaltiesSubject.next(this.penalties);
            }
        });
    }
}
