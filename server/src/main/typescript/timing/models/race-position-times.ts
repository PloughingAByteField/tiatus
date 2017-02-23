import { PositionTime } from '../../models/postion-time.model';
import { Race } from '../../models/race.model';
import { Position } from '../../models/position.model';

export class RacePositionTimes {
  public times: PositionTime[];
  public position: Position;
  public race: Race;
}
