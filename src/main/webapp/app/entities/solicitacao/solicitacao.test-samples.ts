import dayjs from 'dayjs/esm';

import { AnwserStatus } from 'app/entities/enumerations/anwser-status.model';
import { StatusSolicitacao } from 'app/entities/enumerations/status-solicitacao.model';

import { ISolicitacao, NewSolicitacao } from './solicitacao.model';

export const sampleWithRequiredData: ISolicitacao = {
  id: 8955,
  dataProposta: dayjs('2022-09-09T08:24'),
  status: StatusSolicitacao['ACEITOU'],
};

export const sampleWithPartialData: ISolicitacao = {
  id: 19827,
  dataProposta: dayjs('2022-09-08T22:37'),
  status: StatusSolicitacao['CANCELOU'],
  obs: 'productivity',
};

export const sampleWithFullData: ISolicitacao = {
  id: 62451,
  dataProposta: dayjs('2022-09-08T19:15'),
  dataModificacao: dayjs('2022-09-09T04:34'),
  aceitar: AnwserStatus['NAO'],
  status: StatusSolicitacao['AGUARDANDORESPOSTA'],
  obs: 'Concreto Orchestrator',
  valorFrete: 63843,
};

export const sampleWithNewData: NewSolicitacao = {
  dataProposta: dayjs('2022-09-09T17:29'),
  status: StatusSolicitacao['AGUARDANDO'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
