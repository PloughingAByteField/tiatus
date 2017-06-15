import { Injectable } from '@angular/core';

import { Data } from '../model/data.model';

import { PenaltiesService } from '../../penalties/penalties.service';
import { ResultsHttpPenaltiesService } from './penalties-http.service';

@Injectable()
export class ResultsPenaltiesService extends PenaltiesService {

    constructor(protected service: ResultsHttpPenaltiesService) {
        super(service);
    }

    public refresh(): void {
        this.service.getPenaltiesData().subscribe((data: Data) => {
            if (!data.cached) {
                this.penalties = data.data;
                this.penaltiesSubject.next(this.penalties);
            }
        });
    }
}
