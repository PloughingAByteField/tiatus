import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { RacePositionTemplate, convertJsonToRacePositionTemplate }
    from './race-position-template.model';

@Injectable()
export class RacePositionsHttpService {

    public templates: RacePositionTemplate[];

    private endPoint: string = '/rest/racePositionTemplates';
    private headers = new Headers({ 'Content-Type': 'application/json' });

    constructor(private http: Http) {}

    public createTemplate(template: RacePositionTemplate): Promise<RacePositionTemplate> {
        return this.http
            .post(this.endPoint,
            JSON.stringify(template), { headers: this.headers })
            .toPromise()
            .then((res: Response) => {
                if (res.status === 201) {
                    let location: string = res.headers.get('location');
                    let locationParts = location.split('/');
                    let id: number = +locationParts[locationParts.length - 1];
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
            JSON.stringify(template), { headers: this.headers })
            .toPromise()
            .then(() => {
                return template;
            })
            .catch((err) => Promise.reject(err));
    }

    public getTemplates(): Observable<RacePositionTemplate[]> {
        return this.http.get(this.endPoint)
        .map(convertJsonToRacePositionTemplates).share();
    }
}

function convertJsonToRacePositionTemplates(response: Response): RacePositionTemplate[] {
    let jsonTemplates: RacePositionTemplate[] = response.json();
    let templates: RacePositionTemplate[] = new Array<RacePositionTemplate>();
    jsonTemplates.map((json: RacePositionTemplate) => {
      templates.push(convertJsonToRacePositionTemplate(json));
    });
    return templates;
}
