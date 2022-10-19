import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISolicitacao } from '../solicitacao.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../solicitacao.test-samples';

import { SolicitacaoService, RestSolicitacao } from './solicitacao.service';

const requireRestSample: RestSolicitacao = {
  ...sampleWithRequiredData,
  dataProposta: sampleWithRequiredData.dataProposta?.toJSON(),
  dataModificacao: sampleWithRequiredData.dataModificacao?.toJSON(),
};

describe('Solicitacao Service', () => {
  let service: SolicitacaoService;
  let httpMock: HttpTestingController;
  let expectedResult: ISolicitacao | ISolicitacao[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SolicitacaoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Solicitacao', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const solicitacao = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(solicitacao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Solicitacao', () => {
      const solicitacao = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(solicitacao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Solicitacao', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Solicitacao', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Solicitacao', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSolicitacaoToCollectionIfMissing', () => {
      it('should add a Solicitacao to an empty array', () => {
        const solicitacao: ISolicitacao = sampleWithRequiredData;
        expectedResult = service.addSolicitacaoToCollectionIfMissing([], solicitacao);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(solicitacao);
      });

      it('should not add a Solicitacao to an array that contains it', () => {
        const solicitacao: ISolicitacao = sampleWithRequiredData;
        const solicitacaoCollection: ISolicitacao[] = [
          {
            ...solicitacao,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSolicitacaoToCollectionIfMissing(solicitacaoCollection, solicitacao);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Solicitacao to an array that doesn't contain it", () => {
        const solicitacao: ISolicitacao = sampleWithRequiredData;
        const solicitacaoCollection: ISolicitacao[] = [sampleWithPartialData];
        expectedResult = service.addSolicitacaoToCollectionIfMissing(solicitacaoCollection, solicitacao);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(solicitacao);
      });

      it('should add only unique Solicitacao to an array', () => {
        const solicitacaoArray: ISolicitacao[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const solicitacaoCollection: ISolicitacao[] = [sampleWithRequiredData];
        expectedResult = service.addSolicitacaoToCollectionIfMissing(solicitacaoCollection, ...solicitacaoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const solicitacao: ISolicitacao = sampleWithRequiredData;
        const solicitacao2: ISolicitacao = sampleWithPartialData;
        expectedResult = service.addSolicitacaoToCollectionIfMissing([], solicitacao, solicitacao2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(solicitacao);
        expect(expectedResult).toContain(solicitacao2);
      });

      it('should accept null and undefined values', () => {
        const solicitacao: ISolicitacao = sampleWithRequiredData;
        expectedResult = service.addSolicitacaoToCollectionIfMissing([], null, solicitacao, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(solicitacao);
      });

      it('should return initial array if no Solicitacao is added', () => {
        const solicitacaoCollection: ISolicitacao[] = [sampleWithRequiredData];
        expectedResult = service.addSolicitacaoToCollectionIfMissing(solicitacaoCollection, undefined, null);
        expect(expectedResult).toEqual(solicitacaoCollection);
      });
    });

    describe('compareSolicitacao', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSolicitacao(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSolicitacao(entity1, entity2);
        const compareResult2 = service.compareSolicitacao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSolicitacao(entity1, entity2);
        const compareResult2 = service.compareSolicitacao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSolicitacao(entity1, entity2);
        const compareResult2 = service.compareSolicitacao(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
