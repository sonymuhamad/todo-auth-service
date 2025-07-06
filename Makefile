# -------- SETTINGS (edit only if you change repo name) -----
IMAGE          = sonymuhamad/todo-auth-service
export CI_COMMIT_SHA ?= $(shell git log --format="%H" -n 1)
COMMIT_HASH    = ${CI_COMMIT_SHA}      # 7‑char commit id
TAG            ?= $(COMMIT_HASH)                           # override with TAG=…

MVN            := ./mvnw                                   # or just mvn if no wrapper

# ---------------------- TARGETS ----------------------------
.PHONY: compile build push clean

compile:
	@echo "🔧 Compiling Java sources..."
	$(MVN) -B -ntp clean package -DskipTests
	@echo "📦 Renaming JAR to target/app.jar"
	cp $$(ls target/*.jar | head -1) target/app.jar


build: compile
	@echo "🐳 building $(IMAGE):$(TAG)"
	docker build \
	  -t $(IMAGE):$(TAG) .

push: build
	@echo "🚀 Pushing to Docker Hub: $(IMAGE):$(TAG)"
	docker push $(IMAGE):$(TAG)

clean:
	@echo "🧹 Cleaning..."
	$(MVN) clean
	-docker rmi $(IMAGE):$(TAG) 2>/dev/null || true
