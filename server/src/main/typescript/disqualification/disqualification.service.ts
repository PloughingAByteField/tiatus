import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';

import { Disqualification } from './disqualification.model';
import { Data } from '../model/data.model';

import { DisqualificationHttpService } from './disqualification-http.service';

@Injectable()
export class DisqualificationService {
    protected disqualifications: Disqualification[] = new Array<Disqualification>();
    protected disqualificationsSubject: BehaviorSubject<Disqualification[]>
        = new BehaviorSubject<Disqualification[]>(this.disqualifications);

    protected requested: boolean = false;

    constructor(protected service: DisqualificationHttpService) {}

    public getDisqualifications(): BehaviorSubject<Disqualification[]> {
        if (!this.requested) {
            this.requested = true;
            this.refresh();
        }
        return this.disqualificationsSubject;
    }

    public refresh(): void {
        this.service.getDisqualifications().subscribe((data: Data) => {
            this.disqualifications = data.data;
            this.disqualificationsSubject.next(this.disqualifications);
        });
    }
}
