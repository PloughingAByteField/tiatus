import { Injectable } from '@angular/core';
import { Http, URLSearchParams, Response } from '@angular/http';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

import { ConfigData } from '../models/config-data.model';

@Injectable()
export class ConfigService {
    private logo: BehaviorSubject<string> = new BehaviorSubject<string>(null);
    private title: BehaviorSubject<string> = new BehaviorSubject<string>(null);
    private config: Object;
    private observable: Observable<Object>;

    constructor(private http: Http) {}

    public getLogo(): Subject<string>  {
        if (this.logo.getValue() === null) {
            this.getConfig().subscribe((data: Object) => {
                this.config = data;
                this.logo.next(data['logo']);
            });
        }
        return this.logo;
    }

    public getEventTitle(): Subject<string> {
        if (this.title.getValue() === null) {
            this.getConfig().subscribe((data: Object) => {
                this.config = data;
                this.title.next(data['title']);
            });
        }
        return this.title;
    }

    private getConfig(): Observable<Object> {
        if (this.config) {
            return Observable.of(this.config);

        } else if (this.observable) {
            // request in progress
            return this.observable;

        } else {
            this.observable = this.http.get('/tiatus/config/config.json')
                .map((res) => {
                    this.config = res.json();
                    this.observable = null;
                    return this.config;
                }).share();
        }

        return this.observable;
    }
}
