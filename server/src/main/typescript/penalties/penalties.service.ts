import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

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
        this.service.getPenalties().subscribe((penalites: Penalty[]) => {
            if (penalites != null) {
                this.penalties = penalites;
                this.penaltiesSubject.next(this.penalties);
            }
        });
    }
}
