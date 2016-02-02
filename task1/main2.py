import math
import random
import numpy

def unpickle(file):
    import cPickle
    fo = open(file, 'rb')
    dict = cPickle.load(fo)
    fo.close()
    return dict
def cosine_similarity(v1,v2): 
    "compute cosine similarity of v1 to v2: (v1 dot v1)/{||v1||*||v2||)"
    sumxx, sumxy, sumyy = 0., 0., 0.
    for i in range(len(v1)):
        x = float(v1[i]); y = float(v2[i])
        sumxx += x*x
        sumyy += y*y
        sumxy += x*y
    return sumxy/math.sqrt(sumxx*sumyy)
v1,v2 = [3, 45, 7, 2], [2, 54, 13, 15]
batch = unpickle("data_batch_1")["data"]
batch2 = unpickle("data_batch_1")["data"]
def cosine_difference(v1, v2):
	return math.acos(cosine_similarity(v1, v2))
	
def choose_100(batch):
	array[]
	rand = randint(0, len(batch-1))
	array[0] = array.append((batch[rand]))
	for i in range(1,100):
		while not array[i] != array[i-1]
			array.append((batch[rand]))
			chosen = smallestDistance(images[i], images)
		
	return array

		

def smallestDistance(img, batch):
	distance = 1000000.
	itemIndex = 0
	for i in range(len(batch)- 1):
		temporalDistance = cosine_difference(img, batch[i])
		if temporalDistance < distance:
			distance = temporalDistance
			itemIndex = i
	returntable = [itemIndex, distance]
	return returntable
	
def multipleTuplePlanes(n):
	vArray = []
	for i in xrange(n):
		vTuple = []
		vTuple.append(randomVector())
		vTuple.append(randomVector())
		vArray.append(vTuple)
	return vArray
	
		
def multiplePlanes(n):
	vArray = []
	for i in xrange(n):
		vArray.append(randomVector())
	return vArray
	
def randomVector():
	v = []
	for i in xrange(3072):
		v.append(random.random() - 0.5)
	return v
	
def verbalAboveOrBelow(v1, v2):
	if aboveOrBelow(v1,v2) > 0:
		return "above"
	if aboveOrBelow(v1,v2) < 0:
		return "below"
	return "on the plane"
		
def aboveOrBelow(v1, v2):
	dot = numpy.dot(v1, v2)
	if dot > 0:
		return 1
	if dot < 0:
		return -1
	return 0
	
def makeHash(img, hyperplanes):
	hashing = []
	for v in hyperplanes:
		dot = aboveOrBelow(img, v)
		hashing.append(dot)
	return hashing	
	
def makeTupleHash(img, hyperplanes):
	hashing = []
	for vTuple in hyperplanes:
		dot1 = aboveOrBelow(img, vTuple[0])
		dot2 = aboveOrBelow(img, vTuple[1])
		tupleHash = []
		tupleHash.append(dot1)
		tupleHash.append(dot2)
		hashing.append(tupleHash)
	return hashing	
	
def batchMapping(batch, hyperplanes):
	kvStorage = dict()
	tasd = 0
	for img in batch:
		_h = makeHash(img, hyperplanes)
		_hStr = ''.join(str(x) for x in _h)
		if _hStr not in kvStorage:
			tasd = tasd + 1
			kvStorage[_hStr] = list()
		kvStorage[_hStr].append(img)
	print(tasd)
	return kvStorage	
	return 0
def tests():
	r = randomVector()
	test = 0
	for i in range(9999):
		if(numpy.dot(r, batch[i]) > 0):
			test = test + 1
	print(test)
	
tests()
print(v1, v2, cosine_similarity(v1,v2))
print(math.acos(cosine_similarity(batch[0], batch[9999])))
#print(smallestDistance(batch[777], batch))
print(verbalAboveOrBelow(randomVector(), batch[random.randint(0,10000)]))
hyperplanes = multiplePlanes(64)
testing = makeHash(batch[333], hyperplanes)
print(testing)
string = ''.join(str(x) for x in testing)
print(string)
d = batchMapping(batch, hyperplanes)
temp = d[string]

print(d.keys())
print(len(d.keys()))
print(len(temp))
