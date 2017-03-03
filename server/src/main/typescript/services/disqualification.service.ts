import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { Disqualification } from '../models/disqualification.model';

import { DisqualificationHttpService } from '../http-services/disqualification.service';

@Injectable()
export class DisqualificationService {
    protected disqualifications: Disqualification[] = new Array<Disqualification>();
    protected penaltiesSubject: BehaviorSubject<Disqualification[]>
        = new BehaviorSubject<Disqualification[]>(this.disqualifications);

    constructor(protected service: DisqualificationHttpService) {
        this.refresh();
    }

    public getDisqualifications(): BehaviorSubject<Disqualification[]> {
        return this.penaltiesSubject;
    }

    public refresh(): void {
        this.service.getDisqualifications().subscribe((disqualifications: Disqualification[]) => {
            this.disqualifications = disqualifications;
            this.penaltiesSubject.next(this.disqualifications);
        });
    }
}
