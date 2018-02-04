import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class AdminDrawHttpService  {

    constructor(private http: HttpClient) {}
}
