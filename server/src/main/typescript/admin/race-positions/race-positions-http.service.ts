import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { RacePositionTemplate } from './race-position-template.model';

@Injectable()
export class RacePositionsHttpService {

    public templates: RacePositionTemplate[];

    private endPoint: string = '/rest/racePositionTemplates';

    private httpHeader = {
        observe: 'response' as const,
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    private httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
      };

    constructor(protected http: HttpClient) { }

    public createTemplate(template: RacePositionTemplate): Promise<RacePositionTemplate> {
        return this.http
            .post(this.endPoint,
                    template, 
                    this.httpHeader)
            .toPromise()
            .then((res: HttpResponse<RacePositionTemplate>) => {
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
                template,
                this.httpHeader)
            .toPromise()
            .then(() => {
                return template;
            })
            .catch((err) => Promise.reject(err));
    }

    public getTemplates(): Observable<RacePositionTemplate[]> {
        return this.http.get<RacePositionTemplate[]>(this.endPoint, this.httpOptions);
    }
}
