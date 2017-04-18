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
            let updateData: boolean = false;
            if (data.cached) {
                if (data.data.length !== this.penalties.length) {
                    updateData = true;
                }
            } else {
                updateData = true;
            }
            if (updateData) {
                this.penalties = data.data;
                this.penaltiesSubject.next(this.penalties);
            }
        });
    }
}
