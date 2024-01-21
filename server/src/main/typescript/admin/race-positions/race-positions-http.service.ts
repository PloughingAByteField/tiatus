import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { RacePositionTemplate } from './race-position-template.model';
import { CachedHttpService } from '../../http/cached-http.service';
import { Data } from '../../model/data.model';

@Injectable()
export class RacePositionsHttpService extends CachedHttpService {

    public templates: RacePositionTemplate[];

    private endPoint: string = '/rest/racePositionTemplates';
    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    constructor(protected http: HttpClient) {
        super(http);
    }

    public createTemplate(template: RacePositionTemplate): Promise<RacePositionTemplate> {
        return this.http
            .post(this.endPoint,
                JSON.stringify(template), { headers: this.headers })
            .toPromise()
            .then((res: Response) => {
                if (res.status === 201) {
                    const location: string = res.headers.get('location');
                    const locationParts = location.split('/');
                    const id: number = +locationParts[locationParts.length - 1];
                    template.id = id;
                }
                return template;
            })
            .catch((err) => Promise.reject(err));
    }

    public removeTemplate(template: RacePositionTemplate): Promise<RacePositionTemplate> {
        return this.http
            .delete(this.endPoint + '/' + template.id)
            .toPromise()
            .then(() => {
                return template;
            })
            .catch((err) => Promise.reject(err));
    }

    public updateTemplate(template: RacePositionTemplate): Promise<RacePositionTemplate> {
        return this.http
            .put(this.endPoint,
            { headers: this.headers })
            .toPromise()
            .then(() => {
                return template;
            })
            .catch((err) => Promise.reject(err));
    }

    public getTemplates(): Observable<RacePositionTemplate[]> {
        return this.http.get<RacePositionTemplate[]>(this.endPoint);
    }
}
