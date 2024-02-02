import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Penalty } from '../../penalties/penalty.model';

import { PenaltiesHttpService } from '../../penalties/penalties-http.service';

@Injectable()
export class AdjudicatorHttpPenaltiesService extends PenaltiesHttpService {
  
  private httpHeader = {
    observe: 'response' as const,
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(protected http: HttpClient) {
    super(http);
  }

  public createPenalty(penalty: Penalty): Promise<Penalty> {
    return this.http
      .post(this.endPoint,
        penalty,
        this.httpHeader)
      .toPromise()
      .then((res: HttpResponse<Penalty>) => {
        if (res.status === 201) {
          const location: string = res.headers.get('location');
          const locationParts = location.split('/');
          const id: number = +locationParts[locationParts.length - 1];
          penalty.id = id;
        }
        return penalty;
      })
      .catch((err) => Promise.reject(err));
  }

  public removePenalty(penalty: Penalty): Promise<Penalty> {
    return this.http
       .delete(this.endPoint + '/' + penalty.id)
      .toPromise()
      .then(() => {
        return penalty;
      })
      .catch((err) => Promise.reject(err));
  }

  public updatePenalty(penalty: Penalty): Promise<Penalty> {
    return this.http
       .put(this.endPoint + '/' + penalty.id,
          penalty,
          this.httpHeader)
      .toPromise()
      .then(() => {
        return penalty;
      })
      .catch((err) => Promise.reject(err));
  }
}
