import { Injectable } from '@angular/core';
import { Http, URLSearchParams, Response } from '@angular/http';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

import { ConfigHttpService } from './config-http.service';

@Injectable()
export class ConfigService {
    private logo: BehaviorSubject<string> = new BehaviorSubject<string>(null);
    private title: BehaviorSubject<string> = new BehaviorSubject<string>(null);
    private config: Object;

    constructor(private service: ConfigHttpService) {
        this.service.getConfig().subscribe((data: Object) => {
            this.config = data;
            this.logo.next(data['logo']);
            this.title.next(data['title']);
        });
    }

    public getLogo(): Subject<string>  {
        return this.logo;
    }

    public getEventTitle(): Subject<string> {
        return this.title;
    }
}
