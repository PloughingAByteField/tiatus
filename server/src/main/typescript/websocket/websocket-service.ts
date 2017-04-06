import { Injectable, OnInit } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subscription } from 'rxjs/Subscription';

import { WebSocketWSService } from './websocket-ws-service';

@Injectable()
export class WebSocketService implements OnInit {

    constructor(private ws: WebSocketWSService) {
        console.log('con');
        console.log(window.location.hostname);
        let url: string = window.location.host;
        if ('production' !== ENV) {
            // hard code port as webpack dev server cannot proxy websockets
            // https://github.com/chimurai/http-proxy-middleware/issues/112
            console.log('not production');
            url = window.location.hostname + ':8080';
        }
        this.subscribeToUrl(url);
    }

    public ngOnInit() {
        console.log('ws init');
        this.subscribeToUrl(window.location.hostname);
    }

    public subscribeToUrl(url: string): void {
        console.log(url);
        this.ws.createWebSocket('ws://' + url + '/ws').subscribe(
            (data: string) => this.onMessage(data),
            (err) => console.log(err),
            () => console.log('websocket is closed'));
    }

    public sendMessage(message: any): void {
        if (this.ws) {
            this.ws.sendMessage(message);
        }
    }

    private onMessage(data: string): void {
        console.log(data);
    }
}
