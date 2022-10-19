import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRotasOfertas } from '../rotas-ofertas.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../rotas-ofertas.test-samples';

import { RotasOfertasService } from './rotas-ofertas.service';

const requireRestSample: IRotasOfertas = {
  ...sampleWithRequiredData,
};

describe('RotasOfertas Service', () => {
  let service: RotasOfertasService;
  let httpMock: HttpTestingController;
  let expectedResult: IRotasOfertas | IRotasOfertas[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RotasOfertasService);
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

    it('should create a RotasOfertas', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const rotasOfertas = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(rotasOfertas).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RotasOfertas', () => {
      const rotasOfertas = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(rotasOfertas).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RotasOfertas', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RotasOfertas', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RotasOfertas', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRotasOfertasToCollectionIfMissing', () => {
      it('should add a RotasOfertas to an empty array', () => {
        const rotasOfertas: IRotasOfertas = sampleWithRequiredData;
        expectedResult = service.addRotasOfertasToCollectionIfMissing([], rotasOfertas);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rotasOfertas);
      });

      it('should not add a RotasOfertas to an array that contains it', () => {
        const rotasOfertas: IRotasOfertas = sampleWithRequiredData;
        const rotasOfertasCollection: IRotasOfertas[] = [
          {
            ...rotasOfertas,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRotasOfertasToCollectionIfMissing(rotasOfertasCollection, rotasOfertas);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RotasOfertas to an array that doesn't contain it", () => {
        const rotasOfertas: IRotasOfertas = sampleWithRequiredData;
        const rotasOfertasCollection: IRotasOfertas[] = [sampleWithPartialData];
        expectedResult = service.addRotasOfertasToCollectionIfMissing(rotasOfertasCollection, rotasOfertas);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rotasOfertas);
      });

      it('should add only unique RotasOfertas to an array', () => {
        const rotasOfertasArray: IRotasOfertas[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const rotasOfertasCollection: IRotasOfertas[] = [sampleWithRequiredData];
        expectedResult = service.addRotasOfertasToCollectionIfMissing(rotasOfertasCollection, ...rotasOfertasArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rotasOfertas: IRotasOfertas = sampleWithRequiredData;
        const rotasOfertas2: IRotasOfertas = sampleWithPartialData;
        expectedResult = service.addRotasOfertasToCollectionIfMissing([], rotasOfertas, rotasOfertas2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rotasOfertas);
        expect(expectedResult).toContain(rotasOfertas2);
      });

      it('should accept null and undefined values', () => {
        const rotasOfertas: IRotasOfertas = sampleWithRequiredData;
        expectedResult = service.addRotasOfertasToCollectionIfMissing([], null, rotasOfertas, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rotasOfertas);
      });

      it('should return initial array if no RotasOfertas is added', () => {
        const rotasOfertasCollection: IRotasOfertas[] = [sampleWithRequiredData];
        expectedResult = service.addRotasOfertasToCollectionIfMissing(rotasOfertasCollection, undefined, null);
        expect(expectedResult).toEqual(rotasOfertasCollection);
      });
    });

    describe('compareRotasOfertas', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRotasOfertas(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRotasOfertas(entity1, entity2);
        const compareResult2 = service.compareRotasOfertas(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRotasOfertas(entity1, entity2);
        const compareResult2 = service.compareRotasOfertas(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRotasOfertas(entity1, entity2);
        const compareResult2 = service.compareRotasOfertas(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
