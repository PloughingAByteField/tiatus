import { PositionTime } from '../../models/postion-time.model';
import { Race } from '../../races/race.model';
import { Position } from '../../positions/position.model';

export class RacePositionTimes {
  public times: PositionTime[];
  public position: Position;
  public race: Race;
}
