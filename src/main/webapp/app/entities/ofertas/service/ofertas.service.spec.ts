import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IOfertas } from '../ofertas.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ofertas.test-samples';

import { OfertasService, RestOfertas } from './ofertas.service';

const requireRestSample: RestOfertas = {
  ...sampleWithRequiredData,
  dataPostagem: sampleWithRequiredData.dataPostagem?.toJSON(),
  dataColeta: sampleWithRequiredData.dataColeta?.format(DATE_FORMAT),
  dataEntrega: sampleWithRequiredData.dataEntrega?.toJSON(),
  dataModificacao: sampleWithRequiredData.dataModificacao?.toJSON(),
  dataFechamento: sampleWithRequiredData.dataFechamento?.toJSON(),
};

describe('Ofertas Service', () => {
  let service: OfertasService;
  let httpMock: HttpTestingController;
  let expectedResult: IOfertas | IOfertas[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OfertasService);
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

    it('should create a Ofertas', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const ofertas = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ofertas).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ofertas', () => {
      const ofertas = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ofertas).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ofertas', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ofertas', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Ofertas', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOfertasToCollectionIfMissing', () => {
      it('should add a Ofertas to an empty array', () => {
        const ofertas: IOfertas = sampleWithRequiredData;
        expectedResult = service.addOfertasToCollectionIfMissing([], ofertas);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ofertas);
      });

      it('should not add a Ofertas to an array that contains it', () => {
        const ofertas: IOfertas = sampleWithRequiredData;
        const ofertasCollection: IOfertas[] = [
          {
            ...ofertas,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOfertasToCollectionIfMissing(ofertasCollection, ofertas);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ofertas to an array that doesn't contain it", () => {
        const ofertas: IOfertas = sampleWithRequiredData;
        const ofertasCollection: IOfertas[] = [sampleWithPartialData];
        expectedResult = service.addOfertasToCollectionIfMissing(ofertasCollection, ofertas);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ofertas);
      });

      it('should add only unique Ofertas to an array', () => {
        const ofertasArray: IOfertas[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ofertasCollection: IOfertas[] = [sampleWithRequiredData];
        expectedResult = service.addOfertasToCollectionIfMissing(ofertasCollection, ...ofertasArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ofertas: IOfertas = sampleWithRequiredData;
        const ofertas2: IOfertas = sampleWithPartialData;
        expectedResult = service.addOfertasToCollectionIfMissing([], ofertas, ofertas2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ofertas);
        expect(expectedResult).toContain(ofertas2);
      });

      it('should accept null and undefined values', () => {
        const ofertas: IOfertas = sampleWithRequiredData;
        expectedResult = service.addOfertasToCollectionIfMissing([], null, ofertas, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ofertas);
      });

      it('should return initial array if no Ofertas is added', () => {
        const ofertasCollection: IOfertas[] = [sampleWithRequiredData];
        expectedResult = service.addOfertasToCollectionIfMissing(ofertasCollection, undefined, null);
        expect(expectedResult).toEqual(ofertasCollection);
      });
    });

    describe('compareOfertas', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOfertas(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOfertas(entity1, entity2);
        const compareResult2 = service.compareOfertas(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOfertas(entity1, entity2);
        const compareResult2 = service.compareOfertas(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOfertas(entity1, entity2);
        const compareResult2 = service.compareOfertas(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
