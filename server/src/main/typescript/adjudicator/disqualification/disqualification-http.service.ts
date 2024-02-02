import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { Disqualification } from '../../disqualification/disqualification.model';

import { DisqualificationHttpService } from '../../disqualification/disqualification-http.service';

@Injectable()
export class AdjudicatorHttpDisqualificationsService extends DisqualificationHttpService {

  private httpHeader = {
    observe: 'response' as const,
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(protected http: HttpClient) {
    super(http);
  }

  public disqualify(disqualification: Disqualification): Promise<Disqualification> {
    return this.http
      .post(this.endPoint,
        disqualification,
        this.httpHeader)
      .toPromise()
      .then((res: HttpResponse<Disqualification>) => {
        if (res.status === 201) {
          const location: string = res.headers.get('location');
          const locationParts = location.split('/');
          const id: number = +locationParts[locationParts.length - 1];
          disqualification.id = id;
        }
        return disqualification;
      })
      .catch((err) => Promise.reject(err));
  }

  public removeDisqualification(disqualification: Disqualification): Promise<Disqualification> {
    console.log(disqualification);
    return this.http
       .delete(this.endPoint + '/' + disqualification.id)
      .toPromise()
      .then(() => {
        return disqualification;
      })
      .catch((err) => Promise.reject(err));
  }

  public updateDisqualification(disqualification: Disqualification): Promise<Disqualification> {
    return this.http
       .put(this.endPoint + '/' + disqualification.id,
          disqualification,
          this.httpHeader)
      .toPromise()
      .then(() => {
        return disqualification;
      })
      .catch((err) => Promise.reject(err));
  }
}
