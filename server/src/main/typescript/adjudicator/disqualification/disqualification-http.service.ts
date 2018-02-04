import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { Disqualification } from '../../disqualification/disqualification.model';

import { DisqualificationHttpService } from '../../disqualification/disqualification-http.service';

@Injectable()
export class AdjudicatorHttpDisqualificationsService extends DisqualificationHttpService {
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(protected http: HttpClient) {
    super(http);
  }

  public disqualify(disqualification: Disqualification): Promise<Disqualification> {
    return this.http
      .post(this.endPoint,
        JSON.stringify(disqualification), {headers: this.headers})
      .toPromise()
      .then((res: Response) => {
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
        JSON.stringify(disqualification), {headers: this.headers})
      .toPromise()
      .then(() => {
        return disqualification;
      })
      .catch((err) => Promise.reject(err));
  }
}
