import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';

import { Data } from '../model/data.model';

@Injectable()
export class CachedHttpService {

    private previousEtag: string;
    private data: Data = new Data();

    private subject: BehaviorSubject<Data> = new BehaviorSubject<Data>(this.data);

    constructor(protected http: HttpClient) { }

    protected getData(endPoint: string): Observable<Data> {
        this.http.get(endPoint, { observe: 'response' }).subscribe((response: HttpResponse<any>) => {
            if (response.status === 200) {
                const newData: Data = new Data();
                newData.cached = false;
                newData.data = response.body;
                const currentEtag: string = response.headers.get('etag');
                if (this.previousEtag) {
                    if (this.previousEtag === currentEtag) {
                        newData.cached = true;
                    } else {
                        this.previousEtag = currentEtag;
                    }
                } else {
                    this.previousEtag = currentEtag;
                }
                this.subject.next(newData);
            }
        });

        return this.subject;
    }
}
