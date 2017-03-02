import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Penalty } from '../../models/penalty.model';

import { PenaltiesService } from '../../http-services/penalties.service';

@Injectable()
export class AdjudicatorPenaltiesService extends PenaltiesService {
    private headers = new Headers({'Content-Type': 'application/json'});

    constructor(protected http: Http) {
        super(http);
    }

public createPenalty(penalty: Penalty): Promise<Penalty> {
    return this.http
      .post(this.endpoint,
        JSON.stringify(penalty), {headers: this.headers})
      .toPromise()
      .then((res: Response) => {
        if (res.status === 201) {
          let location: string = res.headers.get('location');
          let locationParts = location.split('/');
          let id: number = +locationParts[locationParts.length - 1];
          penalty.id = id;
        }
        return penalty;
      })
      .catch((err) => Promise.reject(err));
  }

  public removePenalty(penalty: Penalty): Promise<Penalty> {
    return this.http
       .delete(this.endpoint + '/' + penalty.id)
      .toPromise()
      .then(() => {
        return penalty;
      })
      .catch((err) => Promise.reject(err));
  }

  public updatePenalty(penalty: Penalty): Promise<Penalty> {
    return this.http
       .put(this.endpoint,
        JSON.stringify(penalty), {headers: this.headers})
      .toPromise()
      .then(() => {
        return penalty;
      })
      .catch((err) => Promise.reject(err));
  }
}
